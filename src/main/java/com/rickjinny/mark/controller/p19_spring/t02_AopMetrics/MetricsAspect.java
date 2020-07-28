package com.rickjinny.mark.controller.p19_spring.t02_AopMetrics;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 实现一个切面完成 Metrics 注解提供的功能。这个切面可以实现标记了 @RestController 注解的 Web 控制器的自动切入，
 * 如果还需要对更多 Bean 进行切入的话，再自行标记 @Metrics 注解。
 */
@Aspect
@Component
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MetricsAspect {
    // 让 Spring 帮我们注入 ObjectMapper，以方便通过 JSON 序列化来记录方法入参和出参
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 实现一个返回 Java 基本类型默认值的工具，其实你也可以逐一写很多 if - else 判断类型，然后手动设置其默认值。
     * 这里为了减少代码量用了一个技巧，即通过初始化一个具有 1 个元素的数组，然后通过获取这个数组的值来获取基本类型默认值。
     */
    private static final Map<Class<?>, Object> DEFAULT_VALUES = Stream
            .of(boolean.class, byte.class, char.class, double.class, float.class, int.class, long.class, short.class)
            .collect(Collectors.toMap(clazz -> (Class<?>) clazz, clazz -> Array.get(Array.newInstance(clazz, 1), 0)));

    public static <T> T getDefaultValue(Class<T> clazz) {
        return (T) DEFAULT_VALUES.get(clazz);
    }

    /**
     * @annotation 指示器实现对标记了 Metrics 注解的方法进行匹配
     */
    @Pointcut("within(@com.rickjinny.mark.controller.p19_spring.t02_AopMetrics.Metrics *)")
    public void withMetricsAnnotation() {

    }

    /**
     * within 指示器实现了匹配那些类型上标记了 @RestController 注解的方法
     */
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerBean() {

    }

    @Around("controllerBean() || withMetricsAnnotation()")
    public Object metrics(ProceedingJoinPoint joinPoint) throws Throwable {
        // 通过连接点获取方法签名和方法上 Metrics 注解，并根据方法签名生成日志中要输出的方法定义描述
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        String name = String.format("[%s] [%s]", signature.getDeclaringType().toString(), signature.toLongString());
        // 因为需要默认对所有 @RestController 标记的 Web 控制器实现 @Metrics 注解的功能
        Metrics metrics = signature.getMethod().getAnnotation(Metrics.class);
        if (metrics == null) {
            metrics = signature.getMethod().getDeclaringClass().getAnnotation(Metrics.class);
        }

        // 对于 Controller 和 Repository，我们需要初始化一个 @Metrics 注解出来
        if (metrics == null) {
            @Metrics
            final class c {
            }
            metrics = c.class.getAnnotation(Metrics.class);
        }

        // 对于 Web 项目，我们可以从上下文中获取到额外的一些信息来丰富我们的日志
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            if (request != null) {
                name += String.format("[%s]", request.getRequestURL().toString());
            }
        }

        // 实现的是入参的日志输出
        if (metrics.logParameters()) {
            log.info(String.format("[入参日志] 调用 %s 的参数是: [%s]", name, objectMapper.writeValueAsString(joinPoint.getArgs())));
        }

        // 实现连接点方法的执行，以及成功失败的打点，出现异常的时候还会记录日历。
        // 这里我们通过日志方式暂时替代了打点的实现，标准的实现是需要把信息对接打点服务，比如：Micrometer
        Object returnValue;
        Instant start = Instant.now();
        try {
            returnValue = joinPoint.proceed();
            if (metrics.recordSuccessMetrics()) {
                log.info(String.format("[成功打点] 调用 %s 成功, 耗时: %s ms", name, Duration.between(start, Instant.now()).toMillis()));
            }
        } catch (Exception e) {
            if (metrics.recordFailMetrics()) {
                log.info(String.format("[失败打点] 调用 %s 失败, 耗时: %d ms", name, Duration.between(start, Instant.now()).toMillis()));
            }

            if (metrics.logException()) {
                log.error(String.format("[异常日志] 调用 %s 出现异常", name), e);
            }

            // 如果忽略异常那么直接返回默认值
            if (metrics.ignoreException()) {
                returnValue = getDefaultValue(signature.getReturnType());
            } else {
                throw e;
            }
        }

        // 实现了返回值的日志输出
        if (metrics.logReturn()) {
            log.info(String.format("[出参日志] 调用 %s 的返回是：[%s]", name, returnValue));
        }
        return returnValue;
    }
}

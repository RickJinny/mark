package com.rickjinny.mark.controller.p20_spring_02.t01_AopFeign;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * AOP 切入 fegin.Client 的实现
 *
 * 使用 Spring Cloud 做微服务调用，为方便统一处理 Feign, 想到了用 AOP 实现，即使用 within 指示器匹配 fegin.Client 接口实现进行 AOP 切入。
 */
@Aspect
@Slf4j
@Component
public class WrongAspect {

    /**
     * 通过 @Before 注解在执行方法前打印日志，并在代码中定义了一个标记了 @FeignClient 注解的 Client 类，让其成为一个 Feign 接口。
     */
    @Before("within(feign.Client+)")
    public void before(JoinPoint joinPoint) {
        log.info("with(feign.Client+) joinPoint {}, args: {}", joinPoint, joinPoint.getArgs());
    }
}

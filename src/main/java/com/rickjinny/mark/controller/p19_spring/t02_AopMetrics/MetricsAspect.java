package com.rickjinny.mark.controller.p19_spring.t02_AopMetrics;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

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

    // 实现一个返回 Java 基本类型默认值的工具，其实你也可以逐一写很多 if - else 判断类型，然后


}

package com.rickjinny.mark.controller.p19_spring_01.t02_AopMetrics;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 通过一个例子，来理解一下增强的执行顺序。
 * 通过 @Order 注解设置优先级为 10;
 * 在内部定义 @Before、@After、@Around 三类增强类，三个增强的逻辑只是简单的日志输出，切点是 MetricsController 的所有方法；
 * 然后定义一个类似的 TestAspectWithOrder20 切面，设置优先级为 20。
 */
@Aspect
@Component
@Order(10)
@Slf4j
public class TestAspectWithOrder10 {

    @Before("execution(* com.rickjinny.mark.controller.p19_spring_01.t02_AopMetrics.MetricsController.*())")
    public void before(JoinPoint joinPoint) {
        log.info("TestAspectWithOrder10 @Before");
    }

    @After("execution(* com.rickjinny.mark.controller.p19_spring_01.t02_AopMetrics.MetricsController.*())")
    public void after(JoinPoint joinPoint) {
        log.info("TestAspectWithOrder10 @After");
    }

    @Around("execution(* com.rickjinny.mark.controller.p19_spring_01.t02_AopMetrics.MetricsController.*())")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("TestAspectWithOrder10 @Around before");
        Object object = proceedingJoinPoint.proceed();
        log.info("TestAspectWithOrder10 @Around after");
        return object;
    }
}

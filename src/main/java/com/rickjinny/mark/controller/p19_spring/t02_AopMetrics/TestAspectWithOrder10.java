package com.rickjinny.mark.controller.p19_spring.t02_AopMetrics;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(10)
@Slf4j
public class TestAspectWithOrder10 {

    @Before("execution(* com.rickjinny.mark.controller.p19_spring.t02_AopMetrics.MetricsController.*())")
    public void before(JoinPoint joinPoint) {
        log.info("TestAspectWithOrder10 @Before");
    }

    @After("execution(* com.rickjinny.mark.controller.p19_spring.t02_AopMetrics.MetricsController.*())")
    public void after(JoinPoint joinPoint) {
        log.info("TestAspectWithOrder10 @After");
    }

    @Around("execution(* com.rickjinny.mark.controller.p19_spring.t02_AopMetrics.MetricsController.*())")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("TestAspectWithOrder10 @Around before");
        Object object = proceedingJoinPoint.proceed();
        log.info("TestAspectWithOrder10 @Around after");
        return object;
    }
}

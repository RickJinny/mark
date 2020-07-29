package com.rickjinny.mark.controller.p19_spring.t02_AopMetrics;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(20)
@Slf4j
public class TestAspectWithOrder20 {


}

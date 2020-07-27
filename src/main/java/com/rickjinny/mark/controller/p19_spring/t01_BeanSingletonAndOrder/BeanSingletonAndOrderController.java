package com.rickjinny.mark.controller.p19_spring.t01_BeanSingletonAndOrder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping(value = "/beanSingletonAndOrder")
@RestController
@Slf4j
public class BeanSingletonAndOrderController {

    @Autowired
    private List<SayService> sayServiceList;

    @Autowired
    private ApplicationContext applicationContext;

    @RequestMapping(value = "test")
    public void test() {
        log.info("============================");
        sayServiceList.forEach(SayService::say);
    }

    @RequestMapping(value = "/test2")
    public void test2() {
        log.info("============================");
        applicationContext.getBeansOfType(SayService.class).values().forEach(SayService::say);
    }
}

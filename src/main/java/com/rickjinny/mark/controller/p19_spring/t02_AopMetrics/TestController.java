package com.rickjinny.mark.controller.p19_spring.t02_AopMetrics;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping(value = "/test")
@RestController
public class TestController {

    @RequestMapping(value = "/test")
    public void test() {
        
    }
}

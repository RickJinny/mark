package com.rickjinny.mark.controller.p05_httpinvoke.t05_FeignPerMethodTimeout;

import feign.Request;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/feignPerMethodTimeout")
@Slf4j
public class FeignPerMethodTimeoutController {

    @Autowired
    private Client client;

    @RequestMapping(value = "/test")
    public void test() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("method1");
        String result1 = client.method1(new Request.Options(1000, 25000));
        stopWatch.stop();
        stopWatch.start("method2");
        String result2 = client.method2(new Request.Options(1000, 3500));
        stopWatch.start();
        log.info("result : {} , result2: {}, time: {}", result1, result2, stopWatch.prettyPrint());
    }
}

package com.rickjinny.mark.controller.p05_httpinvoke.t02_FeignAndRibbonTimeout;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/feignAndRibbon")
@Slf4j
public class FeignAndRibbonController {

    private Client client;

    @RequestMapping(value = "/client")
    public void timeout() {
        long begin = System.currentTimeMillis();
        try {
            client.server();
        } catch (Exception e) {
            log.warn("执行耗时: {} ms 错误: {}", System.currentTimeMillis() - begin, e.getMessage());
        }
    }

    @PostMapping(value = "/server")
    public void server() throws InterruptedException {
        TimeUnit.MINUTES.sleep(10);
    }
}

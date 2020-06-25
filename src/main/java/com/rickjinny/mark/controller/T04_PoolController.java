package com.rickjinny.mark.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/pool")
@Slf4j
public class T04_PoolController {

    @PostConstruct
    public void init() {
        try (Jedis jedis = new Jedis("127.0.0.1", 6379)) {
            Assert.isTrue("Ok".equals(jedis.set("a", "1")), "set a = 1 return OK");
            Assert.isTrue("Ok".equals(jedis.set("b", "2")), "set b = 2 return OK");
        }
    }

    @RequestMapping("/test1")
    public void test1() throws InterruptedException {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                String result = jedis.get("a");
                if (!result.equals("1")) {
                    log.warn("Expect a to be 1 but found {}", result);
                    return;
                }
            }
        }).start();

        new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                String result = jedis.get("b");
                if (!result.equals("2")) {
                    log.warn("Expect b to be 2 but found {}", result);
                    return;
                }
            }
        }).start();
        TimeUnit.SECONDS.sleep(5);
    }
}

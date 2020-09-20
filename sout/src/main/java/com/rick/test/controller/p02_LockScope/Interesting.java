package com.rick.test.controller.p02_LockScope;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Interesting {

    volatile int a = 1;
    volatile int b = 1;

    public void add() {
        log.info("add start");
        for (int i = 0; i < 10000; i++) {
            a++;
            b++;
        }
        log.info("add done");
    }

    public void compare() {
        log.info("compare start");
        for (int i = 0; i < 10000; i++) {
            // a 始终等于 b 吗？
            if (a < b) {
                log.info("a:{}, b:{}, {}", a, b, a > b);
                // 最后的 a > b 应该始终是 false 吗？
            }
        }
        log.info("compare done");
    }
}

package com.rickjinny.mark.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lock")
@Slf4j
public class T02_LockController {

    private volatile int a = 1;
    private volatile int b = 1;

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
                log.info("a:{}, b:{}, {}", a, b, a < b);
                // 最后的 a > b 应该始终是 false 的吗？
            }
        }
        log.info("compare done");
    }

    public static void main(String[] args) {
        T02_LockController lock = new T02_LockController();
        new Thread(() -> lock.add()).start();
        new Thread(() -> lock.compare()).start();
    }
}

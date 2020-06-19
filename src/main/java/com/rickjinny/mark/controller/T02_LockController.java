package com.rickjinny.mark.controller;

import com.rickjinny.mark.bean.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.IntStream;

@RestController
@RequestMapping("/lock")
@Slf4j
public class T02_LockController {

    private volatile int a = 1;
    private volatile int b = 1;

    public synchronized void add() {
        log.info("add start");
        for (int i = 0; i < 10000; i++) {
            a++;
            b++;
        }
        log.info("add done");
    }

    public synchronized void compare() {
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


    /**
     * 2、
     */
    @RequestMapping("/wrong")
    public int wrong(@RequestParam(value = "count", defaultValue = "1000000") int counter) {
        Data.reset();
        // 多线程循环，一定次数调用不同实例的 wrong 方法
        IntStream.rangeClosed(1, counter).parallel()
                .forEach(i -> new Data().wrong());
        return Data.getCounter();
    }
}

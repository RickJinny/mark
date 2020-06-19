package com.rickjinny.mark.controller;

import com.rickjinny.mark.bean.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static java.lang.System.*;

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

    /**
     * 3、精细化加锁，只对 List<Integer> 对象加锁
     */
    private List<Integer> data = new ArrayList<>();

    // 不涉及共享资源的慢方法
    private void slow() {
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 错误的加锁方法
    @RequestMapping("/wrong3")
    public int wrong3() {
        long begin = System.currentTimeMillis();
        IntStream.rangeClosed(1, 1000).parallel().forEach(i -> {
            // 加锁粒度太粗了
            synchronized (this) {
                slow();
                data.add(i);
            }
        });
        log.info("took:{}", System.currentTimeMillis() - begin);
        return data.size();
    }

    // 正确的加锁方法
    @RequestMapping("/right3")
    public int right3() {
        long begin = System.currentTimeMillis();
        IntStream.rangeClosed(1,1000).parallel().forEach(i -> {
            slow();
            // 只对 List 加锁
            synchronized (data) {
                data.add(i);
            }
        });
        log.info("took:{}", System.currentTimeMillis() - begin);
        return data.size();
    }
}
package com.rickjinny.mark.controller.p02_lock.t02_LockGranularity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@RestController
@Slf4j
@RequestMapping(value = "/lockGranularity")
public class LockGranularityController {

    private List<Integer> data = new ArrayList<>();

    /**
     * 不涉及共享资源的慢方法
     */
    private void slow() {
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 错误的加锁方法
     */
    @RequestMapping(value = "/wrong")
    public int wrong() {
        long begin = System.currentTimeMillis();
        IntStream.rangeClosed(1, 1000).parallel().forEach(i -> {
            // 加锁粒度太粗了
            synchronized (this) {
                slow();
                data.add(i);
            }
        });
        log.info("took: {}", System.currentTimeMillis() - begin);
        return data.size();
    }

    /**
     * 正确的加锁方法
     */
    @RequestMapping(value = "/right")
    public int right() {
        long begin = System.currentTimeMillis();
        IntStream.rangeClosed(1, 1000).parallel().forEach(i -> {
            slow();
            // 只对 List 加锁
            synchronized (data) {
                data.add(i);
            }
        });
        log.info("took: {}", System.currentTimeMillis() - begin);
        return data.size();
    }
}

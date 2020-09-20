package com.rick.test.controller.p02_LockGranularity;

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

    private void slow() {
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/wrong")
    public int wrong() {
        long begin = System.currentTimeMillis();
        IntStream.rangeClosed(1, 1000).parallel().forEach(i -> {
            synchronized (this) {
                slow();
                data.add(i);
            }
        });
        log.info("took: {}", System.currentTimeMillis() - begin);
        return data.size();
    }

    @RequestMapping(value = "/right")
    public int right() {
        long begin = System.currentTimeMillis();
        IntStream.rangeClosed(1, 1000).parallel().forEach(i -> {
            slow();
            synchronized (data) {
                data.add(i);
            }
        });
        log.info("took: {}", System.currentTimeMillis() - begin);
        return data.size();
    }
}

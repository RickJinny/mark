package com.rickjinny.mark.controller;

import com.rickjinny.mark.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * 23、缓存设计
 */
@RestController
@RequestMapping("/cache")
@Slf4j
public class T23_RedisCacheController {

    private AtomicInteger atomicInteger = new AtomicInteger();

    @PostConstruct
    public void wrongInit() {
        // 初始化 1000 个城市数据到 Redis, 所有缓存数据有效期是 30 秒
        IntStream.rangeClosed(1, 1000).forEach(i -> RedisUtil.set("city" + i, getCityFromDB(i), 30));
        log.info("Cache init finished");

        // 每秒一次，输出数据库访问的 QPS
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            log.info("DB QPS : {}", atomicInteger.getAndSet(0));
        }, 0, 1, TimeUnit.SECONDS);
    }

    @RequestMapping("/city")
    public String city() {
        // 随机查询一个城市
        int id = ThreadLocalRandom.current().nextInt(1000) + 1;
        String key = "city" + id;
        String data = RedisUtil.get(key);
        if (data == null) {
            // 回源数据库查询
            data = getCityFromDB(id);
            if (StringUtils.isBlank(data)) {
                // 缓存 30 秒过期
                RedisUtil.set(key, data, 30);
            }
        }
        return data;
    }

    private String getCityFromDB(int cityId) {
        // 模拟查询数据库，查一次，计数器加1
        atomicInteger.incrementAndGet();
        return String.valueOf(cityId + System.currentTimeMillis());
    }
}

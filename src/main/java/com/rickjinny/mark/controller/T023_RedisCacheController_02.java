package com.rickjinny.mark.controller;

import com.rickjinny.mark.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 23、缓存设计：缓存穿透问题
 * 某些 key 属于极端热点数据, 并且并发量很大的情况下, 如果这个 key 过期, 可能会在某个瞬间
 * 出现大量的并发请求，大量的请求直接打到 DB 上, 打死 DB。
 * 这种情况, 就是我们常说的缓存击穿或缓存并发问题。
 */
@RestController
@RequestMapping("/cache02")
@Slf4j
public class T023_RedisCacheController_02 {

    /**
     * 为了高并发下, 计数的原子性，使用 AtomicInteger
     */
    private AtomicInteger atomicInteger = new AtomicInteger();

    @PostConstruct
    public void init() {
        // 初始化一个热点数据到 Redis 中, 过期时间设置为5秒
        RedisUtil.set("hotsopt", getExpensiveData(), 5);

        // 每隔1秒输出一下回源的 QPS
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            log.info("DB QPS : {}", atomicInteger.getAndSet(0));
        }, 0, 1, TimeUnit.SECONDS);
    }

    private String getExpensiveData() {
        return null;
    }
}

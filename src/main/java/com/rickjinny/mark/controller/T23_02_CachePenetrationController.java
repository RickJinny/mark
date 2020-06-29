package com.rickjinny.mark.controller;

import com.rickjinny.mark.utils.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * 23、缓存设计：缓存穿透问题
 * 当在缓存中查找不存在的 key 时，会直接打到 DB 中;
 * 当大量请求查找不存在的 key 时, 都打到 DB 上, 会打死 DB
 */
@RestController
@RequestMapping("/cache02")
@Slf4j
public class T23_02_CachePenetrationController {

    AtomicInteger atomicInteger = new AtomicInteger();

    @PostConstruct
    public void init() {
        // 初始化 1000 个城市数据到 Redis, 所有缓存数据有效期是 30 秒
        IntStream.rangeClosed(1, 1000).forEach(i -> RedisClient.set("city" + i, getCityFromDB(i), 30));
        log.info("Cache init finished");

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            log.info("DB QPS : {}", atomicInteger.getAndSet(0));
        }, 0, 1, TimeUnit.SECONDS);
    }

    @GetMapping("/wrong")
    @ResponseBody
    public String wrong(@RequestParam("id") Integer id) {
        String key = "user" + id;
        String data = RedisClient.get(key);
        // 无法区分是无效用户, 还是缓存失效
        if (StringUtils.isEmpty(data)) {
            data = getCityFromDB(id);
            RedisClient.set(key, data, 30);
        }
        return data;
    }

    private String getCityFromDB(Integer id) {
        atomicInteger.incrementAndGet();
        // 注意: 只有 ID 介于 0 (不含) 和 10000 (包含) 之间的用户, 才是有效用户, 可以查询用户信息
        if (id > 0 && id <= 10000) {
            return "userdata";
        }
        return "";
    }
}

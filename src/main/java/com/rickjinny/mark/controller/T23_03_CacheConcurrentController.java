package com.rickjinny.mark.controller;

import com.rickjinny.mark.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 23、缓存设计：缓存击穿问题
 * 某些 key 属于极端热点数据, 并且并发量很大的情况下, 如果这个 key 过期, 可能会在某个瞬间
 * 出现大量的并发请求，大量的请求直接打到 DB 上, 打死 DB。
 * 这种情况, 就是我们常说的缓存击穿或缓存并发问题。
 */
@RestController
@RequestMapping("/cache02")
@Slf4j
public class T23_03_CacheConcurrentController {

    /**
     * 为了高并发下, 计数的原子性，使用 AtomicInteger
     */
    private AtomicInteger atomicInteger = new AtomicInteger();

    @Autowired
    private RedissonClient redissonClient;

    @PostConstruct
    public void init() {
        // 初始化一个热点数据到 Redis 中, 过期时间设置为5秒
        RedisUtil.set("hotKey", getDataFromDB(), 5);

        // 每隔1秒输出一下回源的 QPS
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            log.info("DB QPS : {}", atomicInteger.getAndSet(0));
        }, 0, 1, TimeUnit.SECONDS);
    }

    private String getDataFromDB() {
        atomicInteger.incrementAndGet();
        return "important data";
    }

    @RequestMapping("/wrong")
    public String wrong() {
        String data = RedisUtil.get("hotKey");
        if (StringUtils.isEmpty(data)) {
            data = getDataFromDB();
            // 重新加入缓存, 过期时间还是 5 秒
            RedisUtil.set("hotKey", data, 5);
        }
        return data;
    }

    @RequestMapping("/right")
    public String right() {
        String data = RedisUtil.get("hotKey");
        if (StringUtils.isEmpty(data)) {
            RLock locker = redissonClient.getLock("locker");
            // 获取分布式锁
            if (locker.tryLock()) {
                try {
                    data = RedisUtil.get("hotKey");
                    /**
                     * 双重检查, 因为可能已有一个 B 线程过了第一次判断, 在等锁, 然后 A 线程已经把数据写入 Redis 中
                     */
                    if (StringUtils.isEmpty(data)) {
                        // 回到数据库查询
                        data = getDataFromDB();
                        RedisUtil.set("hotKey", data, 5);
                    }
                } finally {
                    // 别忘记释放, 另外注意写法, 获取锁后整段代码 try + finally, 确保 unlock 万一失
                    locker.unlock();
                }
            }
        }
        return data;
    }
}

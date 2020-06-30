package com.rickjinny.mark.controller.p23;

import com.rickjinny.mark.utils.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * 23、缓存设计：缓存雪崩问题
 */
//@RestController
@RequestMapping("/cache01")
@Slf4j
public class T23_01_CacheInvalidController {

    /**
     * 为了高并发下, 计数的原子性，使用 AtomicInteger
     */
    private AtomicInteger atomicInteger = new AtomicInteger();

    /**
     * 在类中，执行顺序：构造方法 > @Autowired > @PostConstruct
     */
    @PostConstruct
    public void wrongInit() {
        // 初始化 1000 个城市数据到 Redis, 所有缓存数据有效期是 30 秒
        IntStream.rangeClosed(1, 1000).forEach(i -> RedisClient.set("city" + i, getCityFromDB(i), 30));
        log.info("Cache init finished");

        // 每秒一次，输出数据库访问的 QPS
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            log.info("DB QPS : {}", atomicInteger.getAndSet(0));
        }, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * 方案一：设置缓存过期时间，将时间打散，不要让大量的 key 在同一时间内过期。
     * 初始化缓存的时候，设置缓存的过期时间是 30秒 + 10秒以内的随机延迟(扰动值)。
     * 这样，这些 key 不会集中在 30 秒这个时刻过期，而是会分散在 30 - 40 秒之间过期。
     */
    @PostConstruct
    public void rightInit1 () {
        // 这次缓存的过期时间是 30秒 + 10秒内的随机延迟
        IntStream.rangeClosed(1, 1000).forEach(i -> RedisClient.set("city" + i, getCityFromDB(i),
                30 + ThreadLocalRandom.current().nextInt(10)));
        log.info("Cache init finished.");
        // 同样1秒一次输出数据库 QPS
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            log.info("DB QPS : {}", atomicInteger.getAndSet(0));
        }, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * 方案二: 让缓存不主动过期。
     * 初始化缓存数据的时候设置缓存永不过期, 然后启动一个后台线程 30 秒一次定时把所有数据更新到缓存,
     * 而且通过适当的休眠, 控制从数据库更新数据的频率, 降低数据库压力。
     */
    @PostConstruct
    public void rightInit2() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        // 每隔 30秒 全量更新一次缓存
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            IntStream.rangeClosed(1, 1000).forEach(i -> {
                String data = getCityFromDB(i);
                // 模拟更新缓存需要一定的时间
                try {
                    TimeUnit.MILLISECONDS.sleep(20);
                } catch (InterruptedException e) {
                    if (StringUtils.isNotBlank(data)) {
                        // 缓存永不过期，被动更新
                        RedisClient.set("city" + i, data);
                    }
                }
                log.info("Cache update finished");
                // 启动程序的时候需要等待首次更新缓存完成
                countDownLatch.countDown();
            });
        }, 0, 30, TimeUnit.SECONDS);

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            log.info("DB QPS : {}", atomicInteger.getAndSet(0));
        }, 0, 1, TimeUnit.SECONDS);

        countDownLatch.await();
    }

    @RequestMapping("/city")
    public String city() {
        // 随机查询一个城市
        int id = ThreadLocalRandom.current().nextInt(1000) + 1;
        String key = "city" + id;
        String data = RedisClient.get(key);
        // 如果缓存里面不存在的话，到数据库里面查询
        if (data == null) {
            // 回源数据库查询
            data = getCityFromDB(id);
            // 如果数据库里面为可的话
            if (StringUtils.isNotBlank(data)) {
                // 缓存 30 秒过期
                RedisClient.set(key, data, 30);
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

package com.rickjinny.mark.controller.p23;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
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
 * 当大量请求查找不存在的 key 时, 都打到 DB 上, 会打死 DB。
 *
 * 方案一: 对于缓存中不存在的 key, 可以设置一个 value , 比如设置为 null, 下次再查询直接走缓存查询。
 * 方案二: 使用布隆过滤器
 *
 * 方案一的缺点: 可能会大量无效的数据加入缓存中, 如果担心大量无效数据占满缓存的话, 还可以考虑方案二, 即使用布隆过滤器。
 * 方案二的缺点: 需要同步所有可能存在的值并加入布隆过滤器, 这是比较麻烦的地方, 如果业务规定明确的话, 也可以考虑直接根据业务规则判断值是否存在。
 *
 * 比较好的方案:其实可以方案一和方案二同时使用, 即将布隆过滤器前置, 对于误判的情况再保存特殊值到缓存, 双重保险避免无效数据查询请求打到 DB。
 *
 */
@RestController
@RequestMapping("/cache02")
@Slf4j
public class T23_02_CachePenetrationController {

    private AtomicInteger atomicInteger = new AtomicInteger();

    private BloomFilter<Integer> bloomFilter;

    @PostConstruct
    public void init() {
//        // 初始化 1000 个城市数据到 Redis, 所有缓存数据有效期是 30 秒
//        IntStream.rangeClosed(1, 1000).forEach(i -> RedisClient.set("city" + i, getCityFromDB(i), 30));
//        log.info("Cache init finished");

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            log.info("DB QPS : {}", atomicInteger.getAndSet(0));
        }, 0, 1, TimeUnit.SECONDS);

        /**
         * 使用布隆过滤器
         */
        bloomFilter = BloomFilter.create(Funnels.integerFunnel(), 10000, 0.01);
        IntStream.rangeClosed(1, 10000).forEach(bloomFilter::put);
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

    @GetMapping("/right1")
    public String right1(@RequestParam("id") Integer id) {
        String key = "user" + id;
        String data = RedisClient.get(key);
        if (StringUtils.isEmpty(data)) {
            data = getCityFromDB(id);
            // 校验从数据库返回的数据, 是否有效
            if (StringUtils.isNotBlank(data)) {
                RedisClient.set(key, data, 30);
            } else {
                // 如果无效的话，直接在缓存中设置一个NoData, 这样下次查询的时, 即使是无效的用户, 还是可以命中
                RedisClient.set(key, "NoData", 30);
            }
        }
        return data;
    }

    @GetMapping("/right2")
    public String right2(@RequestParam("id") Integer id) {
        String data = "";
        // 通过布隆过滤器先判断
        if (bloomFilter.mightContain(id)) {
            String key = "user" + id;
            // 查缓存
            data = RedisClient.get(key);
            if (StringUtils.isEmpty(data)) {
                // 从数据库查询
                data = getCityFromDB(id);
                RedisClient.set(key, data, 30);
            }
        }
        return data;
    }
}

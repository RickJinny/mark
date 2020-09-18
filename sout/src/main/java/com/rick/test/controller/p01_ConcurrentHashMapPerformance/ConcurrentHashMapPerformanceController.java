package com.rick.test.controller.p01_ConcurrentHashMapPerformance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping(value = "/concurrentHashMapPerformance")
@Slf4j
public class ConcurrentHashMapPerformanceController {
    // 循环次数
    private static int LOOP_COUNT = 10000000;
    // 线程数量
    private static int THREAD_COUNT = 10;
    // 元素数量
    private static int ITEM_COUNT = 10;

    @RequestMapping(value = "/good")
    public String good() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("normalUse");
        Map<String, Long> normalUse = normalUse();
        stopWatch.stop();
        // 校验元素数量
        Assert.isTrue(normalUse.size() == ITEM_COUNT, "normalUse size error");
        // 校验累计总数
        Assert.isTrue(normalUse.entrySet().stream()
                        .mapToLong(item -> item.getValue()).reduce(0, Long::sum) == LOOP_COUNT, "normalUse count error");

        stopWatch.start("goodUse");
        Map<String, Long> goodUse = goodUse();
        stopWatch.stop();
        Assert.isTrue(goodUse.size() == ITEM_COUNT, "goodUse size error");
        Assert.isTrue(goodUse.entrySet().stream()
                        .mapToLong(item -> item.getValue()).reduce(0, Long::sum) == LOOP_COUNT, "goodUse count error");
        log.info(stopWatch.prettyPrint());
        return "OK";
    }

    /**
     * 使用 Map 来统计 key 出现次数的场景。
     * @return
     * @throws InterruptedException
     */
    private Map<String, Long> normalUse() throws InterruptedException {
        ConcurrentHashMap<String, Long> freqs = new ConcurrentHashMap<>(ITEM_COUNT);
        ForkJoinPool forkJoinPool = new ForkJoinPool(THREAD_COUNT);
        forkJoinPool.execute(() -> IntStream.rangeClosed(1, LOOP_COUNT).parallel().forEach(i -> {
            // 获得一个随机的 key
            String key = "item" + ThreadLocalRandom.current().nextInt(ITEM_COUNT);
            synchronized (freqs) {
                if (freqs.containsKey(key)) {
                    // key存在，则+1
                    freqs.put(key, freqs.get(key) + 1);
                } else {
                    // key不存在，则初始化为1
                    freqs.put(key, 1L);
                }
            }
        }));
        forkJoinPool.shutdown();
        forkJoinPool.awaitTermination(1, TimeUnit.HOURS);
        return freqs;
    }

    private Map<String, Long> goodUse() throws InterruptedException {
        ConcurrentHashMap<String, LongAdder> freqs = new ConcurrentHashMap<>(ITEM_COUNT);
        ForkJoinPool forkJoinPool = new ForkJoinPool(THREAD_COUNT);
        forkJoinPool.execute(() -> IntStream.rangeClosed(1, LOOP_COUNT).parallel().forEach(i -> {
            String key = "item" + ThreadLocalRandom.current().nextInt(ITEM_COUNT);
            // 利用 computeIfAbsent() 方法来实例化 LongAdder，然后利用 LongAddr 来进行线程安全计数
            freqs.computeIfAbsent(key, k -> new LongAdder()).increment();
        }));
        forkJoinPool.shutdown();
        forkJoinPool.awaitTermination(1, TimeUnit.HOURS);
        // 因为我们的 value 是 LongAddr 而不是 Long，所以需要做一次转换才能返回。
        return freqs.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().longValue()));
    }
}

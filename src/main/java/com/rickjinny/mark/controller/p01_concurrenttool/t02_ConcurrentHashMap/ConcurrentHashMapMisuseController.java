package com.rickjinny.mark.controller.p01_concurrenttool.t02_ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@Slf4j
@RestController
@RequestMapping(value = "/concurrentHashMapMisuse")
public class ConcurrentHashMapMisuseController {
    // 线程个数
    private static int THREAD_COUNT = 10;
    // 总元素数量
    private static int ITEM_COUNT = 1000;

    // 帮助方法，用来获得一个指定元素数量模拟数据的 ConcurrentHashMap
    private ConcurrentHashMap<String, Long> getData(int count) {
        return LongStream.rangeClosed(1, count)
                .boxed()
                .collect(Collectors.toConcurrentMap(i -> UUID.randomUUID().toString(), Function.identity(),
                        (o1, o2) -> o1, ConcurrentHashMap::new));
    }

    @RequestMapping(value = "/wrong")
    public String wrong() throws InterruptedException {
        ConcurrentHashMap<String, Long> concurrentHashMap = getData(ITEM_COUNT - 100);
        // 初始 900 个元素
        log.info("init size: {}", concurrentHashMap.size());

        ForkJoinPool forkJoinPool = new ForkJoinPool(THREAD_COUNT);
        // 使用线程池并发处理逻辑
        forkJoinPool.execute(() -> IntStream.rangeClosed(1, 10).parallel().forEach(i -> {
            // 查询还需要补充多少元素
            int gap = ITEM_COUNT - concurrentHashMap.size();
            log.info("gap size : {}", gap);
            // 补充元素
            concurrentHashMap.putAll(getData(gap));
        }));
        // 等待所有任务完成
        forkJoinPool.shutdown();
        forkJoinPool.awaitTermination(1, TimeUnit.HOURS);
        // 最后元素个数会是 1000 吗？
        log.info("finish size : {}", concurrentHashMap.size());
        return "OK";
    }

    @RequestMapping(value = "/right")
    public String right() throws InterruptedException {
        ConcurrentHashMap<String, Long> concurrentHashMap = getData(ITEM_COUNT - 100);
        log.info("init size : {}", concurrentHashMap.size());

        ForkJoinPool forkJoinPool = new ForkJoinPool(THREAD_COUNT);
        forkJoinPool.execute(() -> IntStream.rangeClosed(1,10).parallel().forEach(i -> {
            // 下面的这段复合逻辑需要锁一下这个 ConcurrentHashMap
            synchronized (concurrentHashMap) {
                int gap = ITEM_COUNT - concurrentHashMap.size();
                log.info("gap size : {}", gap);
                concurrentHashMap.putAll(getData(gap));
            }
        }));

        forkJoinPool.shutdown();
        forkJoinPool.awaitTermination(1, TimeUnit.HOURS);

        log.info("finish size : {}", concurrentHashMap.size());
        return "OK";
    }
}

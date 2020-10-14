package com.rickjinny.mark.controller.p03_threadpool.t01_ThreadPoolOOM;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping(value = "/threadpool")
@Slf4j
public class ThreadPoolOOMController {

    @RequestMapping(value = "/oom1")
    public void oom1() throws InterruptedException {
        ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        // 打印线程池信息
        printStats(threadPool);

        for (int i = 0; i < 100000000; i++) {
            threadPool.execute(() -> {
                String payload = IntStream.rangeClosed(1, 1000000)
                        .mapToObj(__ -> "a")
                        .collect(Collectors.joining("")) + UUID.randomUUID().toString();
                try {
                    TimeUnit.HOURS.sleep(1);
                } catch (Exception e) {
                    //
                }
                log.info(payload);
            });
        }
        threadPool.shutdown();
        threadPool.awaitTermination(1, TimeUnit.HOURS);
    }

    private void printStats(ThreadPoolExecutor threadPool) {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            log.info("--------------------------------------------------------");
            log.info("Pool Size : {}", threadPool.getPoolSize());
            log.info("Active Threads : {}", threadPool.getActiveCount());
            log.info("Number of Tasks Completed: {}", threadPool.getCompletedTaskCount());
            log.info("Number of Tasks in Queue : {}", threadPool.getQueue().size());
            log.info("--------------------------------------------------------");
        }, 0, 1, TimeUnit.SECONDS);
    }
}

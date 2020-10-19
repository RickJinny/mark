package com.rickjinny.mark.controller.p03_threadpool.t03_ThreadPoolMixuse;

import jodd.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalTime;
import java.util.Collections;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping(value = "threadPoolMixUse")
@Slf4j
public class ThreadPoolMixUseController {

    private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
            2, 2, 1, TimeUnit.HOURS,
            new ArrayBlockingQueue<>(100),
            new ThreadFactoryBuilder().setNameFormat("batchFileProcess-ThreadPool-%d").get(),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    private static ThreadPoolExecutor asyncCalcThreadPool = new ThreadPoolExecutor(
            200, 200, 1, TimeUnit.HOURS,
            new ArrayBlockingQueue<>(1000),
            new ThreadFactoryBuilder().setNameFormat("Async-Calc-threadPool-%d").get()
    );

    private void printStats(ThreadPoolExecutor threadPool) {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            log.info("============================================================");
            log.info("Pool Size : {}", threadPool.getPoolSize());
            log.info("Active Threads : {}", threadPool.getActiveCount());
            log.info("Number of Tasks Completed: {}", threadPool.getCompletedTaskCount());
            log.info("Number of Tasks in Queue: {}", threadPool.getQueue().size());
            log.info("============================================================");
        }, 0, 1, TimeUnit.SECONDS);

    }

    private Callable<Integer> calcTask() {
        return () -> {
            TimeUnit.MILLISECONDS.sleep(10);
            return 1;
        };
    }

    @RequestMapping(value = "/wrong")
    public int wrong() throws ExecutionException, InterruptedException {
        return threadPool.submit(calcTask()).get();
    }

    @RequestMapping(value = "/right")
    public int right() throws ExecutionException, InterruptedException {
        return asyncCalcThreadPool.submit(calcTask()).get();
    }

    @PostConstruct
    public void init() {
        printStats(threadPool);
        new Thread(() -> {
            String payLoad = IntStream.rangeClosed(1, 1000000)
                    .mapToObj(__ -> "a")
                    .collect(Collectors.joining(""));
            while (true) {
                threadPool.execute(() -> {
                    try {
                        Files.write(Paths.get("demo.txt"), Collections.singletonList(LocalTime.now().toString() + ":" + payLoad),
                                StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    log.info("batch file processing done");
                });
            }
        }).start();
    }
}

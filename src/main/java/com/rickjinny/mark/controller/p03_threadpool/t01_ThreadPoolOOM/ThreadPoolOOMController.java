package com.rickjinny.mark.controller.p03_threadpool.t01_ThreadPoolOOM;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
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

    @RequestMapping(value = "/oom2")
    public void oom2() throws InterruptedException {
        ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        printStats(threadPool);
        for (int i = 0; i < 100000000; i++) {
            threadPool.execute(() -> {
                String payload = UUID.randomUUID().toString();
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

    @RequestMapping("/right")
    public int right() throws InterruptedException {
        // 使用一个计数器跟踪完成的任务数
        AtomicInteger atomicInteger = new AtomicInteger();
        // 创建一个具有 2 个核心线程、5个最大线程，使用容量为 10 的 ArrayBlockingQueue 阻塞队列作为工作队列的线程池，使用默认的 AbortPolicy 拒绝策略
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                2, 5, 5, TimeUnit.HOURS,
                new ArrayBlockingQueue<>(10), new ThreadPoolExecutor.AbortPolicy());
        printStats(threadPool);
        // 每隔 1s 提交一次，一共提交 20 次任务
        IntStream.rangeClosed(1, 20).forEach(i -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            }

            int id = atomicInteger.incrementAndGet();
            try {
                threadPool.submit(() -> {
                    log.info("{} started", id);
                    // 每个任务耗时 10 秒
                    try {
                        TimeUnit.SECONDS.sleep(10);
                    } catch (Exception e) {
                        //
                    }
                    log.info("{} finished", id);
                });
            } catch (Exception e) {
                // 提交出现异常的话，打印出错信息并为计数器减一
                log.error("error submitting task {}", id, e);
                atomicInteger.decrementAndGet();
            }
        });
        TimeUnit.SECONDS.sleep(60);
        return atomicInteger.intValue();
    }

    @RequestMapping(value = "/better")
    public int better() throws InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger();
        BlockingQueue<Runnable> queue = new LinkedBlockingDeque<Runnable>(10) {
            @Override
            public boolean offer(Runnable runnable) {
                return false;
            }
        };

        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(2, 5, 5, TimeUnit.SECONDS, queue,
                (r, executor) -> {
                    try {
                        if (!executor.getQueue().offer(r, 0, TimeUnit.SECONDS)) {
                            throw new RejectedExecutionException("ThreadPool queue full, failed to offer " + r.toString());
                        }
                    } catch (Exception e) {
                        Thread.currentThread().interrupt();
                    }
                });

        printStats(threadPool);
        IntStream.rangeClosed(1, 20).forEach(i -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            }

            int id = atomicInteger.incrementAndGet();
            try {
                threadPool.submit(() -> {
                    log.info("{} started", id);
                    try {
                        TimeUnit.SECONDS.sleep(10);
                    } catch (Exception e) {
                        //
                    }
                    log.info("{} finished", id);
                });
            } catch (Exception e) {
                log.error("error submitting task {}", id, e);
                atomicInteger.decrementAndGet();
            }
        });
        TimeUnit.SECONDS.sleep(60);
        return atomicInteger.intValue();
    }
}

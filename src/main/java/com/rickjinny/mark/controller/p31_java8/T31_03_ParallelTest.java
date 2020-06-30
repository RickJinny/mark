package com.rickjinny.mark.controller.p31_java8;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * 并行流: 前面看到的 Stream 操作都是串行的 Stream, 操作只是在一个线程中执行。
 * 此外 Java8 还提供了并行流的功能: 通过 parallel 方法, 一键把 Stream 转换为并行操作提交到线程池处理。
 */
@Slf4j
public class T31_03_ParallelTest {

    /**
     * 通过线程池来处理 1 到 100
     */
    @Test
    public void parallel() {
        IntStream.rangeClosed(1, 100).parallel().forEach(i -> {
            System.out.println(LocalDateTime.now() + " : " + i + " " + Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 使用 threadCount 个线程对某个方法总计执行 taskCount 次的操作案例。用于演示并发情况下的多线程处理性能。
     * 除了会用到并行流, 我们有时也会使用线程池或直接使用线程进行类似操作。为了方便对比各种实现方式, 这里我一次性给出实现此类操作
     * 的五种方式。
     */
    private void increment(AtomicInteger atomicInteger) {
        atomicInteger.incrementAndGet();
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 第一种方式：直接使用线程。
     * 直接把任务按照线程数均匀分割，分配到不同的线程执行，使用 CountDownLatch 来阻塞线程, 直到所有线程都完成操作。
     * 这种方式，需要我们自己分割。
     */
    private int thread(int taskCount, int threadCount) throws InterruptedException {
        // 总操作次数计数器
        AtomicInteger atomicInteger = new AtomicInteger();
        // 使用 CountDownLatch 来等待所有线程执行完成
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        // 使用 IntStream 把数字直接转为 Thread
        IntStream.rangeClosed(1, threadCount).mapToObj( i -> new Thread(() -> {
            // 手动把 taskCount 分成 taskCout 份, 每一份有一个线程执行
            IntStream.rangeClosed(1, taskCount / threadCount).forEach(j -> increment(atomicInteger));
            // 每个线程处理完成自己那部分数据之后, countDown 一次
            countDownLatch.countDown();
        })).forEach(Thread::start);
        // 等到所有线程执行完成
        countDownLatch.await();
        // 查询计数器当前值
        return atomicInteger.get();
    }

    /**
     * 第二种方式: 使用 Executors.newFixedThreadPool 来获得固定线程数的线程池，使用 execute 提交所有任务到线程池执行，
     * 最后关闭线程池等待所有任务执行完成。
     */
    private int threadPool(int taskCount, int threadCount) throws InterruptedException {
        // 总操作次数计算器
        AtomicInteger atomicInteger = new AtomicInteger();
        // 初始化一个线程数量 = threadCount 的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        // 所有任务直接提交到线程池处理
        IntStream.rangeClosed(1, taskCount).forEach(i -> executorService.execute(() -> increment(atomicInteger)));
        // 提交关闭线程池申请, 等待之前所有任务执行完成
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.HOURS);
        // 查询计数器当前值
        return atomicInteger.get();
    }

    /**
     * 第三种方式：使用 ForkJoinPool 而不是普通线程执行任务。
     * ForkJoinPool 和传统的 ThreadPoolExecutor 区别在于: 前者对于 n 并行度有 n 个独立队列, 后者是共享队列。
     * 如果大量执行比较短的任务, ThreadPoolExecutor 的单队列就可能会成为瓶颈。这时, 使用 ForkJoinPool 性能会更好。
     * <p>
     * 因此，ForkJoinPool 更适合大任务分割成许多小任务并行执行的场景, 而 ThreadPoolExecutor 适合许多独立任务并发执行的场景。
     * <p>
     * 在这里，我们先自定义一个具有指定并行数的 ForkJoinPool ，再通过这个 ForkJoinPool 并行执行操作。
     */
    private int forkJoin(int taskCount, int threadCount) throws InterruptedException {
        // 总操作次数计数器
        AtomicInteger atomicInteger = new AtomicInteger();
        // 自定义一个并行度 = threadCount 的 ForkJoinPool
        ForkJoinPool forkJoinPool = new ForkJoinPool(threadCount);
        // 所有任务直接提交到线程池处理
        forkJoinPool.execute(() -> IntStream.rangeClosed(1, taskCount).parallel().forEach(i -> increment(atomicInteger)));
        // 提交关闭线程池申请，等待之前所有任务执行完成
        forkJoinPool.shutdown();
        forkJoinPool.awaitTermination(1, TimeUnit.HOURS);
        // 查询计数器当前值
        return atomicInteger.get();
    }

}

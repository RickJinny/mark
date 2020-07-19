package com.rickjinny.mark.controller.p12_exception.t04_ThreadPoolAndException;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * 线程池常用作异步处理或并行处理，那么把任务提交到线程池处理，任务本身出现异常时会怎么样呢？
 */
@RestController
@RequestMapping(value = "/threadPoolAndException")
@Slf4j
public class ThreadPoolAndExceptionController {

    /**
     * 任务1到4，所在的线程是 test0，任务 6 开始运行在线程 test1。由于我的线程池通过线程工厂为线程使用统一的前缀 test 加上
     * 计数器进行命名，因此从线程名的改变可以知道因为异常的抛出，老线程退出了，线程池只能重新创建一个线程。如果每个异步任务都以
     * 异常结束，那么线程池可能完全起不到线程重用的作用。
     *
     * 因为没有手动捕获异常进行处理，ThreadGroup 帮我们进行了未捕获异常的默认处理，向标准错误输出打印了出现异常的线程名称和异常信息。
     * 显然，这种没有以统一的错误日志格式记录错误信息打印出来的形式，对生产代码是不合适的。
     */
    @GetMapping(value = "/executeWrong")
    public void executeWrong() throws InterruptedException {
        String prefix = "test";
        ExecutorService threadPool = Executors.newFixedThreadPool(1,
                new ThreadFactoryBuilder().setNameFormat(prefix + "%d").build());
        // 提交 10 个任务到线程池处理，第 5 个任务会抛出运行时异常
        IntStream.rangeClosed(1, 10).forEach(i -> threadPool.execute(() -> {
            if (i == 5) throw new RuntimeException("error");
            log.info("I am done: {}", i);
        }));
        threadPool.shutdown();
        threadPool.awaitTermination(1, TimeUnit.HOURS);
    }

    /**
     *
     * 第一种修复的方法：
     * (1)、以 execute 方法提交到线程池的异步任务，最好在任务内部做好异常处理。
     * (2)、设置自定义的异常处理程序作为保底，比如在声明线程池时自定义线程池的未捕获异常处理程序。
     */
    @GetMapping(value = "/executeRight")
    public void executeRight() throws InterruptedException {
        String prefix = "test";
        ExecutorService threadPool = Executors.newFixedThreadPool(1, new ThreadFactoryBuilder()
                .setNameFormat(prefix + "%d")
                .setUncaughtExceptionHandler((thread, throwable) -> log.error("ThreadPool {} got exception", thread, throwable))
                .build());
        // 提交 10 个任务到线程池处理，第 5 个任务会抛出运行时异常
        IntStream.rangeClosed(1, 10).forEach(i -> threadPool.execute(() -> {
            if (i == 5) throw new RuntimeException("error");
            log.info("I am done: {}", i);
        }));
        threadPool.shutdown();
        threadPool.awaitTermination(1, TimeUnit.HOURS);
    }

    /**
     * 第二种修复的方法：设置全局的默认未捕获异常处理程序。
     */
    static {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> log.error("Thread {} got exception", thread, throwable));
    }


}

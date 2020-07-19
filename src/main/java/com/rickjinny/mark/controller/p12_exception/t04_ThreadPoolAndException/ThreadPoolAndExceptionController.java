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

    @GetMapping(value = "execute/")
    public void execute() throws InterruptedException {
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
}

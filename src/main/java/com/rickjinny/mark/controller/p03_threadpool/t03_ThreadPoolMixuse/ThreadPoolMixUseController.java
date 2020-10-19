package com.rickjinny.mark.controller.p03_threadpool.t03_ThreadPoolMixuse;

import jodd.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
    




}

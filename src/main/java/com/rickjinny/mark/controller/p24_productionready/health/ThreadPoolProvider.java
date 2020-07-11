package com.rickjinny.mark.controller.p24_productionready.health;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolProvider {

    /**
     * 一个工作线程的线程池，队列长度为 10
     */
    private static ThreadPoolExecutor demoThreadPool = new ThreadPoolExecutor(
            1,
            1,
            2,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10),
            new ThreadFactoryBuilder().setNameFormat("demo-threadpool-%d").build());

    /**
     * 核心线程数 10，最大线程数 50 的线程池，队列长度 50
     */
    private static ThreadPoolExecutor ioThreadPool = new ThreadPoolExecutor(
            10,
            50,
            2,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(100),
            new ThreadFactoryBuilder().setNameFormat("io-thread-%d").build());


    public static ThreadPoolExecutor getDemoThreadPool() {
        return demoThreadPool;
    }

    public static ThreadPoolExecutor getIoThreadPool() {
        return ioThreadPool;
    }
}

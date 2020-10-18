package com.rickjinny.mark.controller.p03_threadpool.t02_ThreadPoolReuse;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolHelper {

    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            10, 50, 2, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(1000),
            new ThreadFactoryBuilder().setNameFormat("Demo-ThreadPool-%d").build()
    );

    public static ThreadPoolExecutor getThreadPool() {
        return (ThreadPoolExecutor) Executors.newCachedThreadPool();
    }

    public static ThreadPoolExecutor getRightThreadPool() {
        return threadPoolExecutor;
    }
}

package com.rickjinny.mark.controller.p24_productionready.info;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolProvider {

    private static ThreadPoolExecutor demoThreadPool = new ThreadPoolExecutor(
            1, 1,
            2, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10), new ThreadPoolExecutor.AbortPolicy());

}

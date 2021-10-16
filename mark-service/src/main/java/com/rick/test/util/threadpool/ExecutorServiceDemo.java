package com.rick.test.util.threadpool;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ExecutorServiceDemo {

    public static void main(String[] args) {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(16);
        try {
            String res = executorService.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return "哈哈";
                }
            }).get();

            System.out.println("str = " + res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

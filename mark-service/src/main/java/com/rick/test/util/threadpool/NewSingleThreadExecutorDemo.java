package com.rick.test.util.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewSingleThreadExecutorDemo {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 10; i++) {
            final int a = i;
            executorService.execute(() -> {
                System.out.println("start: " + a);
                try {
                    Thread.sleep(1000L);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("end:" + a);
            });
        }
        executorService.shutdown();
        System.out.println("Main Thread End!");
    }

}

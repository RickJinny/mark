package com.rick.test.util.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewFixedThreadPoolDemo {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(16);
        for (int i = 0; i < 50; i++) {
            final int no = i;
            executorService.execute(() -> {
                try {
                    System.out.println("start: " + no);
                    Thread.sleep(1000L);
                    System.out.println("end: " + no);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        executorService.shutdown();
        System.out.println("Main Thread End!");

    }
}

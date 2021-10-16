package com.rick.test.util.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExceptionDemo {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        try {
            Future<Object> future = executorService.submit(() -> {
                throw new RuntimeException("executorService.submit()");
            });

            Double b = (Double) future.get();
            System.out.println(b);
        } catch (Exception e) {
            System.out.println("catch submit");
            e.printStackTrace();
        }

        executorService.shutdown();
        System.out.println("Main Thread End!");

    }
}

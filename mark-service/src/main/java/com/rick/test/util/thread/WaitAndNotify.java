package com.rick.test.util.thread;

public class WaitAndNotify {
    public static void main(String[] args) {
        MethodClass methodClass = new MethodClass();
        Thread thread01 = new Thread(() -> {
            try {
                methodClass.product();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "thread01");

        Thread thread02 = new Thread(() -> {
            try {
                methodClass.customer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "thread02");

        Thread thread03 = new Thread(() -> {
            try {
                methodClass.customer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "thread03");

        thread01.start();
        thread02.start();
        thread03.start();
    }

    private static class MethodClass {
        // 定义生产最大量
        private final int MAX_COUNT = 20;

        private int productCount = 0;

        public synchronized void product() throws InterruptedException {
            while (true) {
                System.out.println(Thread.currentThread().getName() + "-- run --" + productCount);
                Thread.sleep(10);
                if (productCount >= MAX_COUNT) {
                    System.out.println("货船已满，不必再生产");
                    wait();
                } else {
                    productCount++;
                }
                notifyAll();
            }
        }

        public synchronized void customer() throws InterruptedException {
            while (true) {
                System.out.println(Thread.currentThread().getName() + "-- run --" + productCount);
                Thread.sleep(10);
                if (productCount <= 0) {
                    System.out.println("货船已无货，无法消费");
                    wait();
                } else {
                    productCount--;
                }
                notifyAll();
            }
        }

    }
}

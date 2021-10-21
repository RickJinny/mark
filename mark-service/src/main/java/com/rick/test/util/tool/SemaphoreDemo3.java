package com.rick.test.util.tool;

import java.util.concurrent.Semaphore;

public class SemaphoreDemo3 {

    public static void main(String[] args) {
        // 启动线程
        for (int i = 0; i < 10; i++) {
            // 生产者
            new Thread(new Producer()).start();
            // 消费者
            new Thread(new Consumer()).start();
        }
    }

    // 仓库
    private static Warehouse buffer = new Warehouse();

    private static class Producer implements Runnable {

        private static int num = 1;

        @Override
        public void run() {
            int n = num++;
            while (true) {
                try {
                    buffer.put(n);
                    System.out.println(">" + n);
                    // 速度较快，休息 10 毫秒
                    Thread.sleep(10);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class Consumer implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    System.out.println("<" + buffer.take());
                    // 速度较慢，休息 10000 毫秒
                    Thread.sleep(1000);
                } catch (Exception e) {

                }
            }
        }
    }

    private static class Warehouse {
        // 非满锁
        private final Semaphore notFull = new Semaphore(10);
        // 非空锁
        private final Semaphore notEmpty = new Semaphore(0);
        // 核心锁
        private final Semaphore mutex = new Semaphore(1);
        // 库存容量
        private final Object[] items = new Object[10];

        private int putPtr, takePtr, count;

        /**
         * 放库存
         */
        public void put(Object object) throws InterruptedException {
            notFull.acquire();
            mutex.acquire();
            items[putPtr] = object;
            try {
                if (++putPtr == items.length) {
                    putPtr = 0;
                    ++count;
                }
            } finally {
                mutex.release();
                notEmpty.release();
            }
        }

        /**
         * 取库存
         */
        public Object take() throws InterruptedException {
            notEmpty.acquire();
            mutex.acquire();
            Object object = items[takePtr];
            try {
                if (++takePtr == items.length) {
                    takePtr = 0;
                    --count;
                }
                return object;
            } finally {
                mutex.release();
                notFull.release();
            }
        }

    }

}

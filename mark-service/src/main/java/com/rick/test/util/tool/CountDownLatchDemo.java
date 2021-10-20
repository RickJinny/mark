package com.rick.test.util.tool;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(5);
        for (int i = 0; i < 5; i++) {
            new Thread(new ReadNum(i, countDownLatch)).start();
        }
        // 注意跟 CyclicBarrier 不同，这里在主线程 await
        countDownLatch.await(); // 等待所有的线程都执行完，才能执行下面的。这里相当于是一个集合点
        System.out.println("===>各个子线程执行结束。。。。");
        System.out.println("===>主线程执行结束。。。。");
    }


    private static class ReadNum implements Runnable {

        private int id;

        private CountDownLatch countDownLatch;

        public ReadNum(int id, CountDownLatch countDownLatch) {
            this.id = id;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            synchronized (this) {
                System.out.println("id: " + id + Thread.currentThread().getName());
                // countDownLatch.countDown()
                System.out.println("线程组任务: " + id + "，结束，其他任务继续");
                countDownLatch.countDown();
            }
        }
    }
}


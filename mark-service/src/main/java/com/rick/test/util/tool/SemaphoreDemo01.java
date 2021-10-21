package com.rick.test.util.tool;

import java.util.concurrent.Semaphore;

public class SemaphoreDemo01 {

    public static void main(String[] args) {
        int n = 8;
        // 5 是机器数目
        Semaphore semaphore = new Semaphore(1);
        for (int i = 0; i < n; i++) {
            new Worker(i, semaphore).start();
        }
    }

    private static class Worker extends Thread {

        private int num;
        private Semaphore semaphore;

        public Worker(int num, Semaphore semaphore) {
            this.num = num;
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            try {
                // 在子线程里控制资源占用
                semaphore.acquire();
                System.out.println("工人" + this.num + "占用一个机器在生产..." + "  我是线程 " + Thread.currentThread().getName());
                Thread.sleep(2000);
                System.out.println("工人" + this.num + "释放出机器");
                // 在子线程里控制释放资源占用
                semaphore.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

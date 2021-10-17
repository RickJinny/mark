package com.rick.test.util.lock;

import java.util.concurrent.locks.LockSupport;

public class LockSupportDemo {

    public static Object object = new Object();

    private static ChangeObjectThread changeObjectThread01 = new ChangeObjectThread("t1");
    private static ChangeObjectThread changeObjectThread02 = new ChangeObjectThread("t2");

    private static class ChangeObjectThread extends Thread {

        public ChangeObjectThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            synchronized (object) {
                System.out.println("in " + getName());
                LockSupport.park();
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("被中断了");
                }
                System.out.println("继续执行");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        changeObjectThread01.start();
        Thread.sleep(1000);
        changeObjectThread02.start();
        Thread.sleep(3000);
        changeObjectThread01.interrupt();
        LockSupport.unpark(changeObjectThread02);
        changeObjectThread01.join();
        changeObjectThread02.join();
    }
}

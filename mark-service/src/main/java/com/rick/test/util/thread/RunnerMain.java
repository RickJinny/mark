package com.rick.test.util.thread;

public class RunnerMain {

    public static void main(String[] args) {
        Runner01 runner01 = new Runner01();
        Thread thread01 = new Thread(runner01);

        Runner02 runner02 = new Runner02();
        Thread thread02 = new Thread(runner02);

        thread01.start();
        thread02.start();

        thread02.interrupt();

        System.out.println("活跃线程数量: " + Thread.activeCount());

        Thread.currentThread().getThreadGroup().list();
        System.out.println("ThreadGroup: " + Thread.currentThread().getThreadGroup().getParent().activeGroupCount());
        Thread.currentThread().getThreadGroup().getParent().list();

    }
}

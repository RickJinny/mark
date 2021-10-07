package com.rick.test.util.thread;

public class RunnableA implements Runnable {

    @Override
    public void run() {
        System.out.println("我是线程: " + Thread.currentThread().getName() + ", 我是 RunnableA 的 run 方法.");
    }
}

package com.rick.test.util.thread;

public class Runner01 implements Runnable {

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println("进入 Runner01 运行状态 ------  i=" + i);
        }
    }
}

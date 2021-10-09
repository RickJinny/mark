package com.rick.test.util.thread;

public class Runner02 implements Runnable {

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println("进入 Runner02 运行状态 ------  i=" + i);
        }

        // 获取当前线程的状态
        boolean interrupted = Thread.currentThread().isInterrupted();

        // 重置状态
        boolean interrupted1 = Thread.interrupted();

        // 获取当前线程的状态
        boolean interrupted2 = Thread.currentThread().isInterrupted();

        System.out.println("Runner02.run interrupted ===> " + interrupted);
        System.out.println("Runner02.run interrupted1 ===> " + interrupted1);
        System.out.println("Runner02.run interrupted2 ===> " + interrupted2);
    }
}

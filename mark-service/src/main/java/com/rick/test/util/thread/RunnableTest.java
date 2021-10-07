package com.rick.test.util.thread;

public class RunnableTest {

    public static void main(String[] args) {
        RunnableA runnableA = new RunnableA();
        runnableA.run();

        System.out.println("-------------------------------------------------------");

        Thread thread = new Thread(runnableA);
        thread.start();
    }
}

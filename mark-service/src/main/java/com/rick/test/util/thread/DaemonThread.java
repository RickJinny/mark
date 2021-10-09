package com.rick.test.util.thread;

public class DaemonThread {

    public static void main(String[] args) {
        Runner01 task = new Runner01() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Thread thread = Thread.currentThread();
                System.out.println("当线程:" + thread.getName());
            }
        };

        Thread thread = new Thread(task);
        thread.setName("thread-aa");
        thread.setDaemon(false);
        thread.start();
    }
}

package com.rick.test.util.thread;

public class DaemonThread {

    public static void main(String[] args) {
        Runnable task = new Runnable() {
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
        thread.setDaemon(true);
        thread.start();
    }
}

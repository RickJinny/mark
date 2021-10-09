package com.rick.test.util.thread;

public class JoinTest {

    public static void main(String[] args) {
        Object object = new Object();
        MyThread myThread = new MyThread("-- myThread --");
        myThread.setObject(object);
        myThread.start();

        synchronized (object) {
            for (int i = 0; i < 100; i++) {
                if (i == 20) {
                    try {
                        myThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(Thread.currentThread().getName() + " -- " + i);
            }
        }
    }

    static class MyThread extends Thread {
        private String name;
        private Object object;

        public MyThread(String name) {
            this.name = name;
        }

        public void setObject(Object object) {
            this.object = object;
        }

        @Override
        public void run() {
            synchronized (object) {
                for (int i = 0; i < 100; i++) {
                    System.out.println(name + i);
                }
            }
        }
    }
}

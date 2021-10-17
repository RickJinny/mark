package com.rick.test.util.lock;

public class Count03 {

    private byte[] lock01 = new byte[1];
    private byte[] lock02 = new byte[1];

    public int num = 0;

    public void add() {
        synchronized (lock01) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (lock02) {
                num += 1;
            }

            System.out.println(Thread.currentThread().getName() + "_" + num);

        }
    }

    public void lockMethod() {
        synchronized (lock02) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (lock01) {
                num += 1;
            }

            System.out.println(Thread.currentThread().getName() + "_" + num);
        }
    }
}

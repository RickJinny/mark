package com.rick.test.util.lock;

import java.util.concurrent.locks.ReentrantLock;

public class Count {

    private final ReentrantLock reentrantLock = new ReentrantLock();

    public void get() {
        try {
            reentrantLock.lock();
            System.out.println(Thread.currentThread().getName() + " get begin.");
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName() + " get end.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            reentrantLock.unlock();
        }
    }

    public void put() {
        try {
            reentrantLock.lock();
            System.out.println(Thread.currentThread().getName() + " put begin.");
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName() + " put end.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            reentrantLock.unlock();
        }
    }
}

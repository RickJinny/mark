package com.rick.test.util.lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Count02 {

    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public void get() {
        readWriteLock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + " get begin.");
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName() + " get end.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public void put() {
        readWriteLock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + " put begin.");
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName() + " put end.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }
}

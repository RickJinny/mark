package com.rick.test.util.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionDemo {

    private final Lock lock = new ReentrantLock();

    private final Condition notFull = lock.newCondition();

    private final Condition notEmpty = lock.newCondition();

    private Object[] items = new Object[20];

    int putPtr, takePtr, count;

    public void put(Object object) {
        lock.lock();
        try {
            // 当 count 等于数组的大小时，当前线程等待，直到 notFull 通知，再进行生产
            while (count == items.length) {
                notFull.await();
            }
            items[putPtr] = object;
            if (++putPtr == items.length) {
                putPtr = 0;
            }
            ++count;
            notEmpty.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public Object take() {
        lock.lock();
        try {
            // 当 count 为 0, 进入等待，直到 notEmpty 通知，进行消费
            while (count == 0) {
                notEmpty.await();
            }

            Object object = items[putPtr];
            if (++takePtr == items.length) {
                takePtr = 0;
            }
            --count;
            notFull.signal();
            return object;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return null;
    }
}

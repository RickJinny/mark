package com.rick.test.controller.exe.p02_LockScope;

import lombok.Getter;

public class Data {

    @Getter
    private static int counter = 0;

    private static Object locker = new Object();

    public static int reset() {
        counter = 0;
        return counter;
    }

    public synchronized void wrong() {
        counter++;
    }

    public void right() {
        synchronized (locker) {
            counter++;
        }
    }
}

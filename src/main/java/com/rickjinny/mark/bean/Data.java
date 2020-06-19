package com.rickjinny.mark.bean;

import lombok.Getter;

public class Data {

    @Getter
    private static int counter = 0;

    private static Object locker = new Object();

    public static int reset() {
        counter = 0;
        return counter;
    }

    public void wrong() {
        synchronized (locker) {
            counter++;
        }
    }
}

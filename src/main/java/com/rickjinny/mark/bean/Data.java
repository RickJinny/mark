package com.rickjinny.mark.bean;

import lombok.Getter;

public class Data {

    @Getter
    private static int counter = 0;

    public static int reset() {
        counter = 0;
        return counter;
    }

    public synchronized void wrong() {
        counter++;
    }
}

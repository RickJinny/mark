package com.rick.test.util.lock;

public class ThreadA extends Thread {

    private Count03 count03;

    public ThreadA(Count03 count03) {
        this.count03 = count03;
    }

    @Override
    public void run() {
        count03.add();
    }
}

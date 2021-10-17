package com.rick.test.util.lock;

public class ThreadB extends Thread {

    private Count03 count03;

    public ThreadB(Count03 count03) {
        this.count03 = count03;
    }

    @Override
    public void run() {
        count03.lockMethod();
    }
}

package com.rick.test.util.lock;


public class LockMain {

    public static void main(String[] args) {
        Count03 count03 = new Count03();
        ThreadA threadA = new ThreadA(count03);
        threadA.setName("线程A");
        threadA.start();

        ThreadB threadB = new ThreadB(count03);
        threadB.setName("线程B");
        threadB.start();
    }
}

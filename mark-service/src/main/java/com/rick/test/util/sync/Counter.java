package com.rick.test.util.sync;

public class Counter {

    private int sum = 0;

    public void incr() {
        sum++;
    }

    public int getSum() {
        return sum;
    }

    public static void main(String[] args) throws InterruptedException {
        int loop = 10000;

        // test single thread
        Counter counter = new Counter();
        for (int i = 0; i < loop; i++) {
            counter.incr();
        }
        System.out.println("single thread: " + counter.getSum());

        // test multiple threads
        final Counter counter2 = new Counter();
        Thread thread01 = new Thread(() -> {
            for (int i = 0; i < loop / 2; i++) {
                counter2.incr();
            }
        });

        Thread thread02 = new Thread(() -> {
            for (int i = 0; i < loop / 2; i++) {
                counter2.incr();
            }
        });

        thread01.start();
        thread02.start();

        Thread.sleep(300);
        // 当前线程的线程组中的数量 > 2
//        while (Thread.activeCount() > 2) {
//            Thread.yield();
//        }
        System.out.println("multiple threads: " + counter2.getSum());
    }

}

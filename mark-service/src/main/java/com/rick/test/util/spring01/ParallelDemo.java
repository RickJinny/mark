package com.rick.test.util.spring01;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ParallelDemo {

    public static final int COUNT = 5_000_000;

    /**
     * 串行排序
     */
    private static void testSerialize() {
        ArrayList<Object> list = new ArrayList<>(COUNT);
        for (int i = 0; i < COUNT; i++) {
            list.add(UUID.randomUUID().toString());
        }

        System.out.println("开始串行排序");
        // 纳秒, 比毫秒的精度高
        long startTime = System.nanoTime();
        list.stream().sorted().count();

        // 纳秒，结束时间
        long endTime = System.nanoTime();

        long millis = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        System.out.println("串行 Stream 耗时: " + millis + " 毫秒");
    }

    /**
     * 并行排序
     */
    private static void testParallel() {
        ArrayList<Object> list = new ArrayList<>(COUNT);
        for (int i = 0; i < COUNT; i++) {
            list.add(UUID.randomUUID().toString());
        }

        System.out.println("开始并行排序");
        // 纳秒, 比毫秒的精度高
        long startTime = System.nanoTime();
        list.parallelStream().sorted().count();


        // 纳秒，结束时间
        long endTime = System.nanoTime();

        long millis = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        System.out.println("并行 Stream 耗时: " + millis + " 毫秒");
    }

    public static void main(String[] args) {
        // 串行排序
        testSerialize();
        // 并行排序
        testParallel();
    }
}

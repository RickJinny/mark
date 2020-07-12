package com.rickjinny.mark.controller.p10_list.t02_sublist;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 使用 List.subList 进行切片操作，居然会导致 OOM ?
 */
public class SubList {

    private static List<List<Integer>> data = new ArrayList<>();

    private static void oom() {
        for (int i = 0; i < 1000; i++) {
            List<Integer> rawList = IntStream.rangeClosed(1, 10000).boxed().collect(Collectors.toList());
            data.add(rawList.subList(0, 1));
        }
    }

    private static void oomFix() {
        for (int i = 0; i < 1000; i++) {
            List<Integer> rawList = IntStream.rangeClosed(1, 100000).boxed().collect(Collectors.toList());
            data.add(new ArrayList<>(rawList.subList(0, 1)));
        }
    }

    /**
     * [2, 3, 4]
     * [1, 2, 4, 5, 6, 7, 8, 9, 10]
     * java.util.ConcurrentModificationException
     */
    private static void wrong() {
        List<Integer> list = IntStream.rangeClosed(1, 10).boxed().collect(Collectors.toList());
        List<Integer> subList = list.subList(1, 4);
        System.out.println(subList);

        // 移除索引为 1 处的元素
        subList.remove(1);
        System.out.println(list);

        // 添加元素 0
        list.add(0);
        try {
            subList.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 第一种方法：不直接使用 subList 方法返回的 subList，而是重新使用 new ArrayList，在构造方法
     * 传入 subList，来构建一个独立的 ArrayList。
     */
    private static void right1() {
        List<Integer> list = IntStream.rangeClosed(1, 10).boxed().collect(Collectors.toList());
        List<Integer> subList = new ArrayList<>(list.subList(1, 4));
        System.out.println(subList);

        subList.remove(1);
        System.out.println(list);

        list.add(0);
        subList.forEach(System.out::println);
    }

    /**
     * 第二种方法：对于 Java8 使用 Stream 的 skip 和 limit API 来跳过流中的元素，以及限制流中元素的个数，
     * 同样可以达到 subList 切片的目的。
     */
    private static void right2() {
        List<Integer> list = IntStream.rangeClosed(1, 10).boxed().collect(Collectors.toList());
        List<Integer> subList = list.stream().skip(1).limit(3).collect(Collectors.toList());
        System.out.println(subList);

        subList.remove(1);
        System.out.println(list);

        list.add(0);
        subList.forEach(System.out::println);
    }

    public static void main(String[] args) {
        oomFix();
    }
}

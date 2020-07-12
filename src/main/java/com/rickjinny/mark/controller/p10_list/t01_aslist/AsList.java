package com.rickjinny.mark.controller.p10_list.t01_aslist;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 在业务的开发中，我们常常会把原始的数组转换为 List 类数据结构，来继续展开各种 Stream 操作。
 * 第一个坑：不能直接使用 Arrays.asList 来转换基本类型数组。
 * 第二个坑：Arrays.asList 返回的 List 不支持增删操作。
 * 第三个坑：对原始数组的修改，会影响到我们获得的那个 List
 */
@Slf4j
public class AsList {

    // 第一个坑：不能直接使用 Arrays.asList 来转换基本类型数组。
    /**
     * 我们初始化三个数字的 int[] 数组，然后使用 Arrays.asList 把数组转换为 List
     *
     * 打印结果：11:04:08.367 [main] INFO com.rickjinny.mark.controller.p10_list.aslist.AsList - list:[[I@ea30797] size:1 class:class [I
     *
     * 这是一种错误的写法：这样初始化的 List 并不我们期望的包含 3 个数字的 List。通过日志可以发现，这个 List 包含的其实是一个 int 数组，
     * 整个 List 的元素个数是 1, 元素类型是整数数组。
     *
     * 原因是：只能把 int 装箱为 Integer，不可能把 int 数组装箱为 Integer 数组。
     * 我们知道，Arrays.asList 方法传入的是一个泛型 T 类型可变参数，最终 int 数组整体作为了一个对象成为了泛型类型 T
     */
    private static void wrong1() {
        int[] arr = {1, 2, 3};
        List list = Arrays.asList(arr);
        log.info("list:{} size:{} class:{}", list, list.size(), list.get(0).getClass());
    }

    /**
     * 方法一：使用 Java8 的 Arrays.stream 方法来进行转换
     */
    private static void right1A() {
        int[] arr = {1, 2, 3};
        List<Integer> list = Arrays.stream(arr).boxed().collect(Collectors.toList());
        log.info("list:{}, size:{}, class:{}", list, list.size(), list.get(0).getClass());
    }

    /**
     * 方法二：把 int 数组声明为包装类型 Integer 数组
     */
    private static void right1B() {
        Integer[] arr = {1, 2, 3};
        List<Integer> list = Arrays.asList(arr);
        log.info("list:{}, size:{}, class:{}", list, list.size(), list.get(0).getClass());
    }


    // 第二个坑：Arrays.asList 返回的 List 不支持增删操作。
    /**
     * 把三个字符串 1、2、3 构成的字符串数组，使用 Arrays.asList 转换为 List 后，将原始字符串数组的第二个字符修改为 4，
     * 然后为 List 增加一个字符串 5，最后数组和 List 会是怎么样呢？
     *
     * java.lang.UnsupportedOperationException
     * 	at java.util.AbstractList.add(AbstractList.java:148)
     * 	at java.util.AbstractList.add(AbstractList.java:108)
     * 	at com.rickjinny.mark.controller.p10_list.aslist.AsList.wrong2(AsList.java:59)
     * 	at com.rickjinny.mark.controller.p10_list.aslist.AsList.main(AsList.java:67)
     * 11:25:23.808 [main] INFO com.rickjinny.mark.controller.p10_list.aslist.AsList - arr:[1, 4, 3] list:[1, 4, 3]
     *
     * Arrays.asList 返回的 List 并不是我们期望的 java.util.ArrayList，而是 Arrays 的内部类 ArrayList。ArrayList 内部类继承自 AbstractList 类，
     * 并没有覆写父类的 add 方法，而父类中的 add 方法的实现，就是抛出 UnsupportedOperationException 。
     */
    private static void wrong2() {
        String[] arr = {"1", "2", "3"};
        List<String> list = Arrays.asList(arr);
        arr[1] = "4";
        try {
            list.add("5");
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("arr:{} list:{}", Arrays.toString(arr), list);
    }

    // 第三个坑：对原始数组的修改，会影响到我们获得的那个 List
    /**
     * 第三个坑：对原始数组的修改，会影响到我们获得的那个 List。
     *
     * 看一下 ArrayList 的实现，可以发现 ArrayList 其实是直接使用了原始的数组。所以我们要特别的小心，把通过 Arrays.asList 获得的 List 交给
     * 其他方法处理，很容易因为共享了数组，相互修改产生 Bug。
     *
     * 修复的方法比较简单：重新 new 一个 ArrayList 初始化 Arrays.asList 返回的 List 即可。
     *
     * 11:39:58.000 [main] INFO com.rickjinny.mark.controller.p10_list.aslist.AsList - arr:[1, 4, 3] list:[1, 2, 3, 5]
     */
    private static void right2() {
        String[] arr = {"1", "2", "3"};
        List list = new ArrayList(Arrays.asList(arr));
        arr[1] = "4";
        try {
            list.add("5");
        } catch (Exception e) {
            log.error("Error message: {}", e.getMessage(), e);
        }
        log.info("arr:{} list:{}", Arrays.toString(arr), list);
    }

    public static void main(String[] args) {
        right2();
    }
}

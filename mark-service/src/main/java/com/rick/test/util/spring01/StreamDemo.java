package com.rick.test.util.spring01;

import com.alibaba.fastjson.JSON;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StreamDemo {

    private static void print(Object object) {
        System.out.println(JSON.toJSONString(object));
    }

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 12);
        print(list);

        /**
         * Optional
         * 为什么使用 Optional ？ 是为了避免对象为空。
         */
        Optional<Integer> first = list.stream().findFirst();
        boolean present = first.isPresent();
        System.out.println(present);

        System.out.println(first.map(i -> i * 100).orElse(100));


        Integer sum = list.stream().filter(i -> i < 4).distinct().reduce(0, (a, b) -> a + b);
        System.out.println("sum = " + sum);

        /**
         * HashMap 的 key 是不能重复的，后面需要加后面的两个参数: (a, b) -> a，HashMap::new
         * HashMap::new 输出是什么类型的 Map：
         * 如果是 HashMap 则是无序的 Map
         * 如果是 LinkedHashMap 则是有序的 Map。
         */
        HashMap<Integer, Integer> map = list
                .parallelStream()
                .collect(Collectors.toMap(a -> a, a -> (a + 1), (a, b) -> a, HashMap::new));
        print(map);

        map.forEach((k, v) -> System.out.println("key : value = " + k + " : " + v));
        print(map);

        /**
         * parallelStream() 表示并行的 Stream。使用这个就可以把它变成多线程执行的了，只需要改成 parallelStream() 即可。
         */
        map.forEach((k, v) -> System.out.println("key:value = " + k + ":" + v));
        List<Integer> list1 = map
                .entrySet()
                .parallelStream()
                .map(e -> e.getKey() + e.getValue()).collect(Collectors.toList());
        print(list1);

        /**
         * 总结: 第一、Fluent API 继续 Stream
         *      第二、终止操作，得到结果
         */

    }
}

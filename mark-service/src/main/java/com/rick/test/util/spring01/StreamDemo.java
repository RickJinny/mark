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


        Optional<Integer> first = list.stream().findFirst();
        System.out.println(first.map(i -> i * 100).orElse(100));


        Integer sum = list.stream().filter(i -> i < 4).distinct().reduce(0, (a, b) -> a + b);
        System.out.println("sum = " + sum);


        HashMap<Integer, Integer> map = list
                .parallelStream()
                .collect(Collectors.toMap(a -> a, a -> (a + 1), (a, b) -> a, HashMap::new));
        print(map);

        map.forEach((k, v) -> System.out.println("key : value = " + k + " : " + v));
        print(map);

        map.forEach((k, v) -> System.out.println("key:value = " + k + ":" + v));
        List<Integer> list1 = map
                .entrySet()
                .parallelStream()
                .map(e -> e.getKey() + e.getValue()).collect(Collectors.toList());
        print(list1);

        /**
         * parallelStream()
         * 总结: 第一、Fluent API 继续 Stream
         *      第二、终止操作，得到结果
         */

    }
}

package com.rick.test.controller.java.p010_list;

import com.alibaba.fastjson.JSON;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 集合类
 */
public class T040_List {



    public static void main(String[] args) {
        /**
         * 不能直接使用 Arrays.asList 来转换基本类型数组。
         */
        int[] arr = {1, 23, 30};
        List<int[]> list = Arrays.asList(arr);
        System.out.println(JSON.toJSON(list)); // 结果为：[[1,23,30]]

        // 使用 Arrays.stream 或传入 Integer[]
        List<Integer> streamList1 = Arrays.stream(arr).boxed().collect(Collectors.toList());
        System.out.println(JSON.toJSON(streamList1));
        // 使用 Integer[]
        Integer[] arrInteger = {10, 20, 30};
        List<Integer> list2 = Arrays.asList(arrInteger);
        System.out.println(list2);
    }
}

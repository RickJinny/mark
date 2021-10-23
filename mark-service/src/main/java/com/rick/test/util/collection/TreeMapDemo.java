package com.rick.test.util.collection;

import java.util.Comparator;
import java.util.TreeMap;

public class TreeMapDemo {

    public static void main(String[] args) {
        // 按照 key 倒序
        TreeMap<Integer, String> map = new TreeMap<>(Comparator.reverseOrder());
        map.put(3, "val3");
        map.put(2, "val2");
        map.put(1, "val1");
        map.put(5, "val5");
        map.put(4, "val4");
        System.out.println(map);

        // 按照 key 顺序
        TreeMap<Integer, String> map1 = new TreeMap<>(Comparator.naturalOrder());
        map1.putAll(map);
        System.out.println(map1);
    }

}

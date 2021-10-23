package com.rick.test.util.collection;

import java.util.*;

public class LinkedHashMapDemo {

    public static void main(String[] args) {
        // 测试 HashMap
        System.out.println("=====>1、test hash map");
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("name01", "haha01");
        hashMap.put("name02", "haha02");
        hashMap.put("name03", "haha03");
        Set<Map.Entry<String, String>> set = hashMap.entrySet();
        Iterator<Map.Entry<String, String>> iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println("key: " + key + " , value: " + value);
        }

        // 测试 LinkedHashMap
        System.out.println("=====>2、test linked hash map");
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("name01", "haha01");
        linkedHashMap.put("name02", "haha02");
        linkedHashMap.put("name03", "haha03");
        Set<Map.Entry<String, String>> set1 = linkedHashMap.entrySet();
        Iterator<Map.Entry<String, String>> iterator1 = set1.iterator();
        while (iterator1.hasNext()) {
            Map.Entry<String, String> entry = iterator1.next();
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println("key: " + key + " , value: " + value);
        }
    }
}

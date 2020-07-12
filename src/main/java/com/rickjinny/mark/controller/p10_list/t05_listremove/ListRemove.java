package com.rickjinny.mark.controller.p10_list.t05_listremove;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ListRemove {

    private static void removeByIndex(int index) {
        ArrayList<Integer> list = IntStream.rangeClosed(1, 10).boxed().collect(Collectors.toCollection(ArrayList::new));
        System.out.println(list.remove(index));
        System.out.println(list);
    }

    private static void forEachRemoveWrong(Integer index) {
        ArrayList<String> list = IntStream.rangeClosed(1, 10)
                .mapToObj(String::valueOf)
                .collect(Collectors.toCollection(ArrayList::new));
        for (String s : list) {
            if ("2".equals(s)) {
                list.remove(s);
            }
        }
        System.out.println(list);
    }

    private static void forEachRemoveRight() {
        ArrayList<String> list = IntStream.rangeClosed(1, 10)
                .mapToObj(String::valueOf)
                .collect(Collectors.toCollection(ArrayList::new));
        for (Iterator<String> iterator = list.iterator(); iterator.hasNext(); ) {
            String next = iterator.next();
            if ("2".equals(next)) {
                iterator.remove();
            }
        }
        System.out.println(list);
    }

    private static void forEachRemoveRight2() {
        ArrayList<String> list = IntStream.rangeClosed(1, 10)
                .mapToObj(String::valueOf)
                .collect(Collectors.toCollection(ArrayList::new));
        list.removeIf(item -> item.equals("2"));
        System.out.println(list);
    }

    public static void main(String[] args) {
        removeByIndex(1);
    }
}

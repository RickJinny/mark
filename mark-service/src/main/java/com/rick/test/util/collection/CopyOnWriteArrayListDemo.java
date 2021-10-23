package com.rick.test.util.collection;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CopyOnWriteArrayListDemo {

    public static void main(String[] args) {
        // ArrayList, LinkedList, Vector 不安全，运行报错
        // 为什么 Vector 也不安全
//        List<Integer> list = new ArrayList<>();
//        List<Integer> list = new LinkedList<>();
//        List<Integer> list = new Vector<>();

        // 只有 CopyOnWriteArrayList 安全，不报错
        List<Integer> list = new CopyOnWriteArrayList<>();

        for (int i = 0; i < 10000; i++) {
            list.add(i);
        }

        T100 t01 = new T100(list);
        T200 t02 = new T200(list);
        t01.start();
        t02.start();
    }

    public static class T100 extends Thread {
        private List<Integer> list;

        public T100(List<Integer> list) {
            this.list = list;
        }

        @Override
        public void run() {
            for (Integer i : list) {
                System.out.println("T100, i = " + i);
            }
        }
    }

    public static class T200 extends Thread {
        private List<Integer> list;

        public T200(List<Integer> list) {
            this.list = list;
        }

        @Override
        public void run() {
            for (int i = 0; i < list.size(); i++) {
                list.remove(i);
            }
        }
    }
}

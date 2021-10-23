package com.rick.test.util.collection;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CopyOnWriteArrayListDemo02 {

    private static final int THREAD_POOL_MAX_NUM = 10;

    // ArrayList 无法运行
    private List<String> mList = new ArrayList<>();

    public static void main(String[] args) {
        new CopyOnWriteArrayListDemo02().start();
    }

    private void initData() {
        for (int i = 0; i < THREAD_POOL_MAX_NUM; i++) {
            this.mList.add("...... Line " + (i + 1) + " ...... ");
        }
    }

    private void start() {
        initData();
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_MAX_NUM);
        for (int i = 0; i < THREAD_POOL_MAX_NUM; i++) {
            executorService.execute(new ListReader(this.mList));
            executorService.execute(new ListWriter(this.mList, i));
        }
    }

    private static class ListReader implements Runnable {

        private List<String> mList;

        public ListReader(List<String> list) {
            this.mList = list;
        }

        @Override
        public void run() {
            if (this.mList != null) {
                for (String str : this.mList) {
                    System.out.println(Thread.currentThread().getName() + " : " + str);
                }
            }
        }
    }

    private class ListWriter implements Runnable {

        private List<String> mList;

        private int mIndex;

        public ListWriter(List<String> list, int index) {
            this.mList = list;
            this.mIndex = index;
        }

        @Override
        public void run() {
            if (this.mList != null) {
                this.mList.add("...... add " + mIndex + ".......");
            }
        }
    }

}

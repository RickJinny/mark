package com.rickjinny.mark.designpattern.p01_chain;

public class Main {

    public static void main(String[] args) {
        Handler handler01 = new Handler01();
        Handler handler02 = new Handler02();
        Handler handler03 = new Handler03();

        handler02.setNextHandler(handler03);
        handler01.setNextHandler(handler02);

        // 处理事件
        System.out.println("message01");
        handler01.handleMessage(1);
        System.out.println("message02");
        handler01.handleMessage(2);
        System.out.println("message03");
        handler01.handleMessage(4);
        System.out.println("message04");
        handler01.handleMessage(8);
    }
}

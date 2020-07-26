package com.rickjinny.mark.controller.p18_advancedfeatures.t02_GenericAndInheritance;

import com.rickjinny.mark.controller.p18_advancedfeatures.t02_GenericAndInheritance.bean.Child01;
import com.rickjinny.mark.controller.p18_advancedfeatures.t02_GenericAndInheritance.bean.Child02;

import java.util.Arrays;

/**
 * 2、泛型经过类型擦除多出桥接方法的坑
 */
public class GenericAndInheritanceApplication {

    /**
     * 子类方法的调用是通过反射进行，实例化 Child01 类型后，通过 getClass().getMethods() 获得所有的方法；
     * 然后按照方法名过滤出 setValue 方法，进行调用，传入字符串 test 作为参数。
     *
     * 结果：
     * Child01.setValue called
     * Parent.setValue called
     * Parent.setValue called
     * value:test updateCount: 2
     * 运行代码后可以看到，虽然 Parent 的 value 字段正确设置了 test，但父类的 setValue 方法
     * 调用了两次，计数器也显示 2 而不是 1。
     */
    private static void wrong1() {
        Child01 child01 = new Child01();
        Arrays.stream(child01.getClass().getMethods())
                .filter(method -> method.getName().equals("setValue"))
                .forEach(method -> {
                    try {
                        method.invoke(child01, "test");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        System.out.println(child01.toString());
    }

    /**
     * 使用 getDeclaredMethods() 方法
     * Child01.setValue called
     * Parent.setValue called
     * value:test updateCount: 1
     */
    private static void wrong2() {
        Child01 child01 = new Child01();
        Arrays.stream(child01.getClass().getDeclaredMethods())
                .filter(method -> method.getName().equals("setValue"))
                .forEach(method -> {
                    try {
                        method.invoke(child01, "test");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        System.out.println(child01.toString());
    }

    /**
     * 使用 Child02 类
     * Child02.setValue called
     * Parent.setValue called
     * Child02.setValue called
     * Parent.setValue called
     * value:test updateCount: 2
     */
    private static void wrong3() {
        Child02 child02 = new Child02();
        Arrays.stream(child02.getClass().getDeclaredMethods())
                .filter(method -> method.getName().equals("setValue"))
                .forEach(method -> {
                    try {
                        method.invoke(child02, "test");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        System.out.println(child02.toString());
    }

    /**
     * 使用 method 的 isBridge() 方法，来判断方法是不是桥接方法：
     * 1、通过 getDeclaredMethods 方法获取到所有方法后，必须同时根据方法名 setValue 和 非 isBridge 两个条件过滤，才能实现唯一过滤。
     * 2、使用 Stream 时，如果希望只匹配 0 或 1 项的话，可以考虑 ifPresent 来使用 findFirst 方法。
     *
     * 结果：
     * Child02.setValue called
     * Parent.setValue called
     * value:test updateCount: 1
     */
    private static void right() {
        Child02 child02 = new Child02();
        Arrays.stream(child02.getClass().getDeclaredMethods())
                .filter(method -> method.getName().equals("setValue") && !method.isBridge())
                .findFirst()
                .ifPresent(method -> {
                    try {
                        method.invoke(child02, "test");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        System.out.println(child02.toString());
    }

    public static void main(String[] args) {
//        wrong1();
//        wrong2();
//        wrong3();
        right();
    }
}

package com.rickjinny.mark.controller.p18_advancedfeatures.t02_GenericAndInheritance;

import com.rickjinny.mark.controller.p18_advancedfeatures.t02_GenericAndInheritance.bean.Child01;

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


    public static void main(String[] args) {
        wrong1();
    }
}

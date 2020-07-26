package com.rickjinny.mark.controller.p18_advancedfeatures.t02_GenericAndInheritance;

/**
 * 子类 Child01 的实现是这样的：继承父类，但是没有父类泛型参数；定义了一个参数为 String 的 setValue 方法，
 * 通过 super.setValue 调用父类方法实现日志记录。我们也能明白，开发同学这么设计是希望覆盖父类的 setValue 实现。
 */
public class Child01 extends Parent {

    public void setValue(String value) {
        System.out.println("Child01.setValue called");
        super.setValue(value);
    }
}

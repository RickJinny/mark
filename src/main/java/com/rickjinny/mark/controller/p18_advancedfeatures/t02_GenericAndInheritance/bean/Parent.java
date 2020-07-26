package com.rickjinny.mark.controller.p18_advancedfeatures.t02_GenericAndInheritance.bean;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 父类 Parent：有一个泛型占位符 T; 有一个 AtomicInteger 计数器，用来记录 value 字段更新的次数，
 * 其中 value 字段是泛型 T 类型的，setValue 方法每次为 value 赋值时对计数器进行 +1 操作。
 * 重写了 toString 方法，输出 value 字段的值和计数器的值。
 */
public class Parent<T> {
    // 用于记录 value 更新的次数，模拟日志记录的逻辑
    private AtomicInteger updateCount = new AtomicInteger();

    private T value;

    /**
     * 重写 toString，输出值和值更新次数
     */
    @Override
    public String toString() {
        return String.format("value:%s updateCount: %d", value, updateCount.get());
    }

    /**
     * 设置 value 值
     */
    public void setValue(T value) {
        System.out.println("Parent.setValue called");
        this.value = value;
        updateCount.incrementAndGet();
    }
}

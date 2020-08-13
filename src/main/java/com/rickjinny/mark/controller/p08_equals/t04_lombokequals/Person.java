package com.rickjinny.mark.controller.p08_equals.t04_lombokequals;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Lombok 的 @Data 注解会帮我们实现 equals 和 hashcode 方法，
 * 但是有继承关系时 Lombok 自动生成的方法可能就不是我们期望的了。
 */
@Data
public class Person {

    /**
     * 使用 equals 判等会得到 false。如果希望只要身份证一致就是同一个人，可以使用 @EqualsAndHashCode.Exclude 注解
     * 来修饰 name 字段，从 equals 和 hashCode 的实现中排除 name 字段。
     */
    @EqualsAndHashCode.Exclude
    private String name;

    private String identity;

    public Person(String name, String identity) {
        this.name = name;
        this.identity = identity;
    }
}

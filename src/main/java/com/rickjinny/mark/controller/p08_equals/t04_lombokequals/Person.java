package com.rickjinny.mark.controller.p08_equals.t04_lombokequals;

import lombok.Data;

/**
 * Lombok 的 @Data 注解会帮我们实现 equals 和 hashcode 方法，但是有继承关系时 Lombok 自动生成的方法可能就不是我们期望的了。
 */
@Data
public class Person {

    private String name;

    private String identity;

    public Person(String name, String identity) {
        this.name = name;
        this.identity = identity;
    }
}

package com.rickjinny.mark.controller.p08_equals.t04_lombokequals;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Lombok 的 @Data 注解会帮我们实现 equals 和 hashcode 方法，
 * 但是有继承关系时 Lombok 自动生成的方法可能就不是我们期望的了。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Employee extends Person {

    private String company;

    public Employee(String name, String identity, String company) {
        super(name, identity);
        this.company = company;
    }
}

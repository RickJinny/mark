package com.rickjinny.mark.controller.p08_equals.t04_lombokequals;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Lombok 的 @Data 注解会帮我们实现 equals 和 hashcode 方法，
 * 但是有继承关系时 Lombok 自动生成的方法可能就不是我们期望的了。
 *
 * 两个 Employee 实例，他们具有相同的公司名称，但是姓名和身份证均不同。运行发现为 true。
 * 说明 @EqualsAndHashCode 默认实现没有使用父类属性。可以在 @EqualsAndHashCode 注解上，手动加一个开关 callSuper 为 true。
 * 加上 callSuper = true 之后，实现了同时以子类的属性 company 加上父类中的属性 identity，作为 equals 和 hashCode 方法的实现条件。
 *
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

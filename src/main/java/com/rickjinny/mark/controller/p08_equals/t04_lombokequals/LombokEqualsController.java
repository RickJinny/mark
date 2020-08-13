package com.rickjinny.mark.controller.p08_equals.t04_lombokequals;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/lombokEquals")
@Slf4j
public class LombokEqualsController {

    public static void test1() {
        Person person01 = new Person("wang", "001");
        Person person02 = new Person("zhang", "001");
        //  person01.equals(person02) ? false
        log.info("person01.equals(person02) ? {} ", person01.equals(person02));
    }

    public static void test2() {
        /**
         * 两个 Employee 实例，他们具有相同的公司名称，但是姓名和身份证均不同。运行发现为 true。
         * 说明 @EqualsAndHashCode 默认实现没有使用父类属性。
         * 可以在 @EqualsAndHashCode 注解上，手动加一个开关 callSuper 为 true。
         */
        Employee employee01 = new Employee("xiaowang", "001", "haha.com");
        Employee employee02 = new Employee("xiaozhang", "002", "haha.com");
        // false
        System.out.println(employee01.equals(employee02));
    }

    public static void main(String[] args) {
//        test1();
        test2();
    }
}

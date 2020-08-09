package com.rickjinny.mark.controller.p09_numeralcalculations.t03_equals;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@RestController
@RequestMapping(value = "/equals")
@Slf4j
public class BigDecimalEqualsController {

    /**
     * 使用 equals 方法，比较 1.0 和 1 这两个 BigDecimal
     */
    private static void wrong() {
        boolean b = new BigDecimal("1.0").equals(new BigDecimal("1")); // false
        System.out.println(b);
    }

    /**
     * 只比较 BigDecimal 的 value 值，可以使用 compareTo 方法
     */
    private static void right() {
        BigDecimal b1 = new BigDecimal("1.0");
        BigDecimal b2 = new BigDecimal("1");
        boolean b = b1.compareTo(b2) == 0;  // true
        System.out.println(b);
    }

    private static void set() {
        /**
         * 把 1.0 的 BigDecimal 加入 HashSet，然后判断其是否存在值为 1 的 BigDecimal，得到的结果是 false。
         */
        Set<BigDecimal> set1 = new HashSet<>();
        set1.add(new BigDecimal("1.0"));
        System.out.println(set1.contains(new BigDecimal("1"))); // 返回false

        /**
         * 解决方法一：
         * 把 BigDecimal 存入 HashSet 或 HashMap 前，先使用 stripTrailingZeros() 方法去掉尾部的零。
         * 比较的时候也去掉尾部的 0，确保 value 相同的 BigDecimal，scale 也是一致的。
         */
        Set<BigDecimal> set2 = new HashSet<>();
        set2.add(new BigDecimal("1.0").stripTrailingZeros());
        System.out.println(set2.contains(new BigDecimal("1.000").stripTrailingZeros())); // 返回 true

        /**
         * 解决方法二：
         * 使用 TreeSet 替换 HashSet。TreeSet 不使用 hashCode 方法，也不使用 equals 比较元素，而是使用 compareTo 方法，
         * 所有不会有问题。
         */
        Set<BigDecimal> treeSet = new TreeSet<>();
        treeSet.add(new BigDecimal("1.0"));
        System.out.println(treeSet.contains(new BigDecimal("1"))); // 返回 true
    }

    public static void main(String[] args) {
//        wrong();
//        right();
        set();
    }
}

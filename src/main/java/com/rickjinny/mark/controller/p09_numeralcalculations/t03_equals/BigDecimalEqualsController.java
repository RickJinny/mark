package com.rickjinny.mark.controller.p09_numeralcalculations.t03_equals;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

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

    public static void main(String[] args) {
//        wrong();
        right();
    }
}

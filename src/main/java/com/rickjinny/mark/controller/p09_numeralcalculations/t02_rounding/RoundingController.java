package com.rickjinny.mark.controller.p09_numeralcalculations.t02_rounding;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(value = "/rounding")
public class RoundingController {

    /**
     * 用 double 和 float 初始化两个 3.35 的浮点数，然后通过 String.format() 使用 %.1f 来格式化这 2 个数字。
     * 原因：这是由于精度和舍入方式共同导致的，double 和 float 的 3.35，其实相当于 3.350xxx 和 3.349xxx。
     * String.format 采用四舍五入的方式进行舍入，取 1 位小数时，double的3.350四舍五入为 3.4，而 float 的 3.349 四舍五入为 3.3。
     */
    private static void wrong1() {
        double num1 = 3.35;
        double num2 = 3.35f;
        // 四舍五入
        System.out.println(String.format("%.1f", num1)); // 3.4
        System.out.println(String.format("%.1f", num2)); // 3.3
    }

    public static void main(String[] args) {
        wrong1();
    }
}

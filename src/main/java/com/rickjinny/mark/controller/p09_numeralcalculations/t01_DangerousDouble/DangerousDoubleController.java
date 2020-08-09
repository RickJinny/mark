package com.rickjinny.mark.controller.p09_numeralcalculations.t01_DangerousDouble;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(value = "/dangerousDouble")
public class DangerousDoubleController {

    /**
     * 对简单的浮点数，进行加减乘除
     * 结果：
     * 0.30000000000000004
     * 0.19999999999999996
     * 401.49999999999994
     * 1.2329999999999999
     * no
     */
    private static void wrong1() {
        System.out.println(0.1 + 0.2);
        System.out.println(1.0 - 0.8);
        System.out.println(4.015 * 100);
        System.out.println(123.3 / 100);

        double amount1 = 2.15;
        double amount2 = 1.10;
        if (amount1 - amount2 == 1.05) {
            System.out.println("OK");
        } else {
            System.out.println("no");
        }
    }

    public static void main(String[] args) {
        wrong1();
    }
}

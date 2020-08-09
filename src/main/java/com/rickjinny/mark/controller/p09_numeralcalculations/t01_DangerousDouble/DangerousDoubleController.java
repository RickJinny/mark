package com.rickjinny.mark.controller.p09_numeralcalculations.t01_DangerousDouble;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

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
     *
     * 出现这样情况的原因是：计算机是以二进制存储数值的，浮点数也不例外，Java 采用了 IEEE 754 标准实现浮点数的表达和运算。
     * 对于计算机而言，0.1 无法精确表达，这是浮点数计算造成精度损失的根源。
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

    /**
     * 在使用金额计算的时候，使用 BigDecimal 类型，浮点数精确表达和运算场景，一定使用这个类型。
     * 不过，在使用 BigDecimal 时有几个坑需要避开。
     *
     * 0.3000000000000000166533453693773481063544750213623046875
     * 0.1999999999999999555910790149937383830547332763671875
     * 298.0999999999999872102307563181966543197631835937500
     * 0.3189999999999999857891452847979962825775146484375
     */
    private static void wrong2() {
        System.out.println(new BigDecimal(0.1).add(new BigDecimal(0.2)));
        System.out.println(new BigDecimal(1.0).subtract(new BigDecimal(0.8)));
        System.out.println(new BigDecimal(2.981).multiply(new BigDecimal(100)));
        System.out.println(new BigDecimal(31.9).divide(new BigDecimal(100)));
    }

    

    public static void main(String[] args) {
//        wrong1();
        wrong2();
    }
}

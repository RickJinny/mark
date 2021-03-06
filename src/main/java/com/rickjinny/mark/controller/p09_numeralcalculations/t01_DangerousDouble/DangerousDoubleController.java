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

    /**
     * 在使用 BigDecimal 表示和计算浮点数，且务必使用字符串的构造方法来初始化 BigDecimal
     *
     * 结果：
     * 0.3
     * 0.2
     * 298.100
     * 0.319
     */
    private static void right() {
        System.out.println(new BigDecimal("0.1").add(new BigDecimal("0.2")));
        System.out.println(new BigDecimal("1.0").subtract(new BigDecimal("0.8")));
        System.out.println(new BigDecimal("2.981").multiply(new BigDecimal("100")));
        System.out.println(new BigDecimal("31.9").divide(new BigDecimal("100")));
    }

    /**
     * BigDecimal 有 scale 和 precision 的概念：
     * scale 表示小数点右边的位数；precision表示精度。
     */
    private static void testScale() {
        BigDecimal b1 = new BigDecimal("100");
        BigDecimal b2 = new BigDecimal(String.valueOf(100d));
        BigDecimal b3 = new BigDecimal(String.valueOf(100));
        BigDecimal b4 = BigDecimal.valueOf(100d);
        BigDecimal b5 = new BigDecimal(Double.toString(100));

        print(b1); //scale 0 precision 3 result 401.500
        print(b2); //scale 1 precision 4 result 401.5000
        print(b3); //scale 0 precision 3 result 401.500
        print(b4); //scale 1 precision 4 result 401.5000
        print(b5); //scale 1 precision 4 result 401.5000
    }

    private static void print(BigDecimal bigDecimal) {
        log.info("scale {}, precision {}, result {}", bigDecimal.scale(), bigDecimal.precision(),
                bigDecimal.multiply(new BigDecimal("4.015")));
    }

    public static void main(String[] args) {
//        wrong1();
//        wrong2();
//        right();
        testScale();
    }
}

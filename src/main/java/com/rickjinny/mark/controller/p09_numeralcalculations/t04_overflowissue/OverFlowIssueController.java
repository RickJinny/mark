package com.rickjinny.mark.controller.p09_numeralcalculations.t04_overflowissue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

/**
 * 4、小心数值溢出问题
 */
@RestController
@Slf4j
@RequestMapping(value = "/overFlowIssue")
public class OverFlowIssueController {

    /**
     * 数值计算溢出的问题：不管是 int 还是 long，所有的基本数值类型都有超出表达范围的可能性。
     * 比如：对 Long 的最大值进行 +1 操作。
     * 下面就发生了溢出，没有任何异常。
     */
    private static void wrong1() {
        long a = Long.MAX_VALUE;
        System.out.println(a + 1); // -9223372036854775808
        System.out.println(a + 1 == Long.MIN_VALUE); // true
    }

    /**
     * 使用 Math 类的 addExact 等 xxExact 方法进行数值运算，这些方法可以在数值溢出时主动抛出异常。
     */
    private static void right1() {
        try {
            long a = Long.MAX_VALUE;
            System.out.println(Math.addExact(a, 1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用大数类 BigInteger。
     * BigDecimal 是处理浮点数的类型。
     * BigInteger 是处理大数的类型。
     * 通过 BigInteger 对 Long 的最大值加1，一点问题都没有，当尝试把结果转换为 Long 类型时，则会提示 BigInteger out of long range
     */
    private static void right2() {
        BigInteger i = new BigInteger(String.valueOf(Long.MAX_VALUE));
        System.out.println(i.add(BigInteger.ONE).toString());

        try {
            long l = i.add(BigInteger.ONE).longValueExact();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        wrong1();
//        right1();
        right2();
    }
}

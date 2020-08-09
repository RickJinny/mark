package com.rickjinny.mark.controller.p08_equals.t01_IntAndStringEqual;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(value = "/intAndStringEqual")
public class IntAndStringEqualController {

    /**
     * int、Integer 类型判等
     */
    public static void intCompare() {
        /**
         * 1、使用 == 对两个值为 127 的，直接赋值的 Integer 对象判等。
         * 编译器会把 Integer a = 127 转换为 Integer.valueOf(127)。
         * 源码里，其实对这个转换做了缓存，使得两个 Integer 指向同一个对象，所以 == 返回 true。
         */
        Integer a = 127; // Integer.valueOf(127)
        Integer b = 127; // Integer.valueOf(127)
        System.out.println( a == b); // true

        /**
         * 2、使用 == 对两个值为 128 的，直接赋值的 Integer 对象判等。
         * 默认情况下，会缓存 [-128, 127] 的数值，而 128 处于这个区间之外。设置-XX:AutoBoxCacheMax=1000 即可。
         */
        Integer c = 128; // Integer.valueOf(128)
        Integer d = 128; // Integer.valueOf(128)
        System.out.println(c == d); // false

        /**
         * 3、Integer e = 127 和 另一个 new Integer 声明的值为 127 的对象判等。
         * 分析: new 出来的 Integer 是不走缓存的新的对象。
         * 一个是缓存的对象，一个是 new 出来的 Integer 对象，结果肯定不是相同的对象，返回为 false。
         */
        Integer e = 127;
        Integer f = new Integer(127);
        System.out.println(e == f); // false

        /**
         * 4、比较 new Integer() 声明的值为 127 的对象判等。
         * 分析：两个new 出来的 Integer 对象，肯定不是一个对象，结果为 false。
         */
        Integer g = new Integer(127);
        Integer h = new Integer(127);
        System.out.println(g == h); // false

        /**
         * 5、一个值为 128 的直接赋值的 Integer 对象和另一个值为 128 的 int 基本类型判等。
         * 分析：基本类型int 的 128, 装箱类型的 128, 装箱类型会自动拆箱，比较肯定是数值而不是引用，因此返回 true。
         */
        Integer i = 128;
        int j = 128;
        System.out.println(i == j); // true
    }

    /**
     * String 类型判等
     */
    public static void stringCompare() {
        /**
         * 1、对两个 String 类型值为 1，使用 == 判等。
         * 常量池机制
         */
        String a = "1";
        String b = "1";
        System.out.println(a == b); // true

        /**
         * 2、对两个 new 出来的值都为 2 的 String，使用 == 判等。
         * new 出来是两个不同的对象。
         */
        String c = new String("2");
        String d = new String("2");
        System.out.println(c == d); // false

        /**
         * 3、对两个 new 出来值都为 3 的 String 先进行 intern 操作，再使用 == 判等。
         * 使用 intern 方法会走常量池机制，所以为true
         */
        String e = new String("3").intern();
        String f = new String("3").intern();
        System.out.println(e == f); // true

        /**
         * 4、对两个 new 出来的值都为 4 的 String 通过 equals 判等。
         * equals 是判断内容，所以是true.
         */
        String g = new String("4");
        String h = new String("4");
        System.out.println(g.equals(h)); // true
    }

    /**
     * 订单状态枚举和 OrderQuery 的 status 都是包装类型，所以通过 == 判等肯定是有问题的。
     * 第一、枚举是 CREATE(1000, "已创建")，容易让人误解 status 值是基本类型；
     * 第二、Integer 有缓存机制存在，所以使用 == 判等并不是所有情况下都有问题，当枚举值超过 127 时就会出现问题。
     */
    @RequestMapping(value = "/enumCompare")
    public void enumCompare(@RequestParam OrderQuery orderQuery) {
        StatusEnum statusEnum = StatusEnum.DELIVERED;
        log.info("orderQuery : {}, statusEnum : {}, result : {}", orderQuery, statusEnum,
                statusEnum.status == orderQuery.getStatus());
    }

    public static void main(String[] args) {
        intCompare();
        stringCompare();
    }
}

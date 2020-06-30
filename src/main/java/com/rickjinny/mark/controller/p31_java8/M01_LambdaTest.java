package com.rickjinny.mark.controller.p31_java8;

import com.google.common.base.Function;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 31、Java8 中的重要知识点
 */
public class M01_LambdaTest {

    @Test
    public void lambdaMethod() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("a1");
            }
        }).start();


        new Thread(() -> System.out.println("a2")).start();
    }

    public static void main(String[] args) {
        // 使用 Lambda 表达式提供的 Supplier 接口实现, 返回 OK 字符串
        System.out.println("------------------------------------");
        System.out.println("1、使用 Lambda 表达式提供的 Supplier 接口实现, 返回 OK 字符串: ");
        Supplier<String> stringSupplier = () -> "OK";
        System.out.println(stringSupplier.get());

        // 使用方法引用提供的 Supplier 接口实现, 返回空字符串
        System.out.println("------------------------------------");
        System.out.println("2、使用方法引用提供的 Supplier 接口实现, 返回空字符串: ");
        Supplier<String> supplier = String::new;
        System.out.println(supplier.get());

        // Predicate 接口是输入一个参数, 返回布尔值, 我们通过 and 方法组合两个 Predicate 条件, 判断是否值大于 0 并且是偶数
        System.out.println("------------------------------------");
        System.out.println("3、Predicate 接口是输入一个参数, 返回布尔值, 我们通过 and 方法组合两个 Predicate 条件, 判断是否值大于 0 并且是偶数: ");
        Predicate<Integer> positiveNumber = i -> i > 0;
        Predicate<Integer> evenNumber = i -> i % 2 == 0;
        System.out.println(positiveNumber.and(evenNumber).test(2));

        // Consumer 接口是消费一个数据, 我们通过 andThen 方法组合调用两个 Consumer， 输出两行 abcdfg
        System.out.println("------------------------------------");
        System.out.println("4、Consumer 接口是消费一个数据, 我们通过 andThen 方法组合调用两个 Consumer， 输出两行 abcdfg : ");
        Consumer<String> println = System.out::println;
        println.andThen(println).accept("abcdfg");

        //Function 接口是输入一个数据, 计算后输出一个数据, 我们先把字符串转换为大写, 然后通过 andThen 组合另一个 Function 实现字符串拼接
        Function<String, String> upperCase = String::toUpperCase;
        Function<String, String> duplicate = s -> s.concat(s);
        System.out.println(upperCase.andThen(duplicate).apply("test"));

        // Supplier 是提供一个数据的接口， 这里我们实现获取一个随机数
        Supplier<Integer> random = () -> ThreadLocalRandom.current().nextInt();
        System.out.println(random.get());

        // BinaryOperator 是输入两个同类型的参数, 输出一个同类型参数的接口, 这里我们通过方法引用获得一个整数加法操作,
        // 通过 Lambda 表达式定义一个减法操作, 然后依次调用
        BinaryOperator<Integer> add = Integer::sum;
        BinaryOperator<Integer> sub = (a, b) -> a - b;
        System.out.println(add.apply(1, 2));
        System.out.println(sub.apply(1, 2));


        /**
         * 使用 Java8 简化代码:
         * 1、使用 Stream 简化集合操作。
         * 2、使用 Optional 简化判空逻辑。
         * 3、JDK8 结合 Lambda 和 Stream 对各种类的增强。
         */
    }
}

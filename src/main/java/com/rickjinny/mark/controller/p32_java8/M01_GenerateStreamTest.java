package com.rickjinny.mark.controller.p32_java8;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * 生成 Stream 流有五种方法：
 * 第一种方法: 通过 stream 方法把 List 或数组转换为流。
 * 第二种方法: 通过 Stream.of 方法直接传入多个元素构成一个流。
 * 第三种方法: 通过 Stream.iterate 方法使用迭代的方式构造一个无限流, 然后使用 limit 限制流元素个数。
 * 第四种方法: 通过 Stream.generate 方法从外部传入一个提供元素的 Supplier 来构造无限流, 然后使用 limit 限制流元素个数。
 * 第五种方法: 通过 IntStream 或 DoubleStream 构造基本类型的流。
 */
@Slf4j
public class M01_GenerateStreamTest {

    /**
     * 第一种方法: 通过 stream 方法把 List 或数组转换为流。
     */
    @Test
    public void stream() {
        Lists.newArrayList("a", "b", "c").stream().forEach(System.out::println);
        Arrays.stream(new int[]{1, 2, 3}).forEach(System.out::println);
    }

    /**
     * 第二种方法: 通过 Stream.of 方法直接传入多个元素构成一个流。
     */
    @Test
    public void of() {
        String[] arr = {"a", "b", "c"};
        Stream.of(arr).forEach(System.out::println);
        Stream.of("a", "b", "c").forEach(System.out::println);
        Stream.of(1, 2, "a").forEach(System.out::println);
        Stream.of(1, 2, "a").map(item -> item.getClass().getName()).forEach(System.out::println);
    }

    /**
     * 第三种方法: 通过 Stream.iterate 方法使用迭代的方式构造一个无限流, 然后使用 limit 限制流元素个数。
     */
    @Test
    public void iterate() {
        Stream.iterate(2, item -> item * 2).limit(10).forEach(System.out::println);
        Stream.iterate(BigInteger.ZERO, n -> n.add(BigInteger.TEN)).limit(10).forEach(System.out::println);
    }

    /**
     * 第四种方法: 通过 Stream.generate 方法从外部传入一个提供元素的 Supplier 来构造无限流, 然后使用 limit 限制流元素个数。
     */
    @Test
    public void generate() {
        Stream.generate(() -> "test").limit(3).forEach(System.out::println);
        Stream.generate(Math::random).limit(10).forEach(System.out::println);
    }

    /**
     * 第五种方法: 通过 IntStream 或 DoubleStream 构造基本类型的流。
     */
    @Test
    public void primitive() {
        // 演示 IntStream 和 DoubleStream
        IntStream.range(1, 3).forEach(System.out::println);
        IntStream.range(0, 3).mapToObj(i -> "x").forEach(System.out::println);

        IntStream.rangeClosed(1, 3).forEach(System.out::println);
        DoubleStream.of(1.1, 2.2, 3.3).forEach(System.out::println);

        // 各种转换, 后面注释代表了输出结果
        Class<? extends int[]> aClass1 = IntStream.of(1, 2).toArray().getClass();
        System.out.println(aClass1); // class [I
        Class<? extends int[]> aClass2 = Stream.of(1, 2).mapToInt(Integer::intValue).toArray().getClass();
        System.out.println(aClass2); // class [I
        Class<? extends Object[]> aClass3 = IntStream.of(1, 2).boxed().toArray().getClass();
        System.out.println(aClass3); // class [Ljava.lang.Object;
        Class<? extends double[]> aClass4 = IntStream.of(1, 2).asDoubleStream().toArray().getClass();
        System.out.println(aClass4); // class [D
        Class<? extends long[]> aClass5 = IntStream.of(1, 2).asLongStream().toArray().getClass();
        System.out.println(aClass5); // class [J

        // 注意基本类型流和装箱后的流的区别
        Arrays.asList("a", "b", "c").stream()
                .mapToInt(String::length)
                .asLongStream()
                .mapToDouble(x -> x / 10.0)
                .boxed()
                .mapToLong(x -> 1L)
                .mapToObj(x -> "")
                .collect(Collectors.toList());
    }
}

package com.rickjinny.mark.controller.p31_java8;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * 使用 Java8 简化代码:
 * 1、使用 Stream 简化集合操作。
 * 2、使用 Optional 简化判空逻辑。
 * 3、JDK8 结合 Lambda 和 Stream 对各种类的增强。
 */
public class T31_02_StreamTest {

    private static double calc(List<Integer> ints) {
        // 临时中间集合
        List<Point2D> point2DList = new ArrayList<>();
        for (Integer i : ints) {
            point2DList.add(new Point2D.Double((double) i % 3, (double) i / 3));
        }
        // 临时变量，纯粹是为了获得最后的结果需要的中间变量
        double total = 0;
        int count = 0;
        for (Point2D point2D : point2DList) {
            // 过滤
            if (point2D.getY() > 1) {
                // 算距离
                double distance = point2D.distance(0, 0);
                total += distance;
                count++;
            }
        }
        // 注意 count 可能为 0 的可能
        return count > 0 ? total / count : 0;
    }

    /**
     * 1、使用 Stream 流, 来简化 calc(List<Integer> ints) 方法的代码逻辑
     */
    @Test
    public void stream() {
        ArrayList<Integer> ints = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8);
        double result = ints.stream()
                .map(i -> new Point2D.Double((double) i % 3, (double) i / 3))
                .filter(point -> point.getY() > 1)
                .mapToDouble(point -> point.distance(0, 0))
                .average()
                .orElse(0);
        System.out.println(result);

    }

    /**
     * 2、使用 Optional 简化判空逻辑。
     */
    @Test(expected = IllegalArgumentException.class)
    public void optional() {
        // 通过 get 方法获取 Optional 中的设计值
        Integer integer = Optional.of(1).get();

        // 通过 ofNullable 来初始化一个 null, 通过 orElse 方法实现 Optional 中无数据的时候返回一个默认值
        Optional.ofNullable(null).orElse("A");

        // OptionalDouble 是基本类型 double 的 Optional 对象，isParent判断有数据
        OptionalDouble.empty().isPresent();

        // 通过 map 方法，可以对 Optional 对象进行级联转换, 不会出现空指针, 转换后还是一个 Optional
        Integer integer1 = Optional.of(1).map(Math::incrementExact).get();

        // 通过 filter 方法实现 Optional 中数据的过滤, 得到一个 Optional, 然后级联使用 orElse 提供默认值
        Integer integer2 = Optional.of(1).filter(i -> i % 2 == 0).orElse(null);

        // 通过 orElseThrow 实现无数据时抛出异常
        Optional.empty().orElseThrow(IllegalAccessError::new);

    }


    /**
     * 3、结合 Lambda 和 Stream 对各种类的增强。
     */

    private Map<Long, Product> cache = new ConcurrentHashMap<>();

    private Product getProductAndCache(Long id) {
        Product product = null;
        // key 存在, 返回 value
        if (cache.containsKey(id)) {
            product = cache.get(id);
        } else {
            // 不存在, 则获取 value。需要遍历数据源查询获得 Product
            for (Product p : Product.getData()) {
                if (p.getId().equals(id)) {
                    product = p;
                    break;
                }
            }
            // 加入 ConcurrentHashMap
            if (product != null) {
                cache.put(id, product);
            }
        }
        return product;
    }


    /**
     * 利用 ConcurrentHashMap 的 computeIfAbsent 方法，一行代码就可以实现。
     */
    private Product getProductAndCacheCool(Long id) {
        return cache.computeIfAbsent(id, i -> // 当key 不存在时，提供一个 Function 来代表根据 key 获取 value 的过程
                Product.getData().stream()
                        .filter(p -> p.getId().equals(i)) // 过滤
                        .findFirst() // 找到第一个, 得到 Optional<Product>
                        .orElse(null) // 如果找不到 Product, 则使用 null
        );
    }

    /**
     * 利用 Files.walk 返回一个 Path 的流, 通过两行代码就能实现递归搜索 + grep 的操作。
     * 整个逻辑是: 递归搜索文件夹, 查找所有的 .java 文件; 然后读取文件每一行内容, 用正则
     * 表达式匹配 public class 关键字; 最后输出文件名和这行内容
     */
    @Test
    public void filesExample() throws IOException {
        // 无限深度, 递归遍历文件夹
        try (Stream<Path> pathStream = Files.walk(Paths.get("."))) {
            pathStream.filter(Files::isRegularFile)
                    .filter(FileSystems.getDefault().getPathMatcher("glob:**/*.java")::matches) // 搜索 java 源码文件
                    .flatMap(ThrowingFunction.unchecked(path ->
                            Files.readAllLines(path).stream() // 读取文件内容, 转换为 Stream<List>
                                    .filter(line -> Pattern.compile("public class").matcher(line).find()) // 使用正则过滤带有 public class 的行
                                    .map(line -> path.getFileName() + " >> " + line))) // 把这行文件内容转换为文件名 + 行
                    .forEach(System.out::println); // 打印所有行
        }
    }

    @FunctionalInterface
    public interface ThrowingFunction<T, R, E extends Throwable> {

        static <T, R, E extends Throwable> Function<T, R> unchecked(ThrowingFunction<T, R, E> f) {
            return t -> {
                try {
                    return f.apply(t);
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            };
        }
        
        R apply(T t) throws E;
    }
}

package com.rickjinny.mark.controller.p32_java8;

import com.rickjinny.mark.controller.p32_java8.bean.Order;
import com.rickjinny.mark.controller.p32_java8.bean.OrderItem;
import com.rickjinny.mark.controller.p32_java8.bean.Product;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Stream 流中具体 api 的使用:
 * 1、filter 方法
 * 2、map 方法
 * 3、flatMap 方法
 * 4、sorted 方法
 * 5、distinct 方法
 * 6、skip & limit
 */
public class M02_StreamDetailTest {

    private static Random random = new Random();

    private List<Order> orders;

    @Before
    public void data() {
        orders = Order.getData();
        orders.forEach(System.out::println);
        System.out.println("==========================================");
    }

    /**
     * 1、filter 方法使用：
     * filter 方法可以实现过滤操作，类似 SQL 中的 where 。
     * 我们可以使用一行代码，通过 filter 方法实现查询所有订单中最近半年金额大于 40 的订单, 通过连续叠加 filter 方法进行多次条件过滤。
     */
    @Test
    public void filter() {
        System.out.println("最近半年，金额大于40的订单");
        orders.stream()
                .filter(Objects::nonNull) // 过滤 null 值
                .filter(order -> order.getPlaceAt().isAfter(LocalDateTime.now().minusMinutes(6))) // 最近半年的订单
                .filter(order -> order.getTotalPrice() > 40) // 过滤金额大于 40 的订单
                .forEach(System.out::println);
    }

    /**
     * 2、map 方法使用
     * map 操作可以做转换（或者说投影）, 类似 SQL 中的 select。
     * 为了对比, 使用两种方式统计订单中所有商品的数量, 前一种是通过两次遍历实现, 后一种是通过两次 mapToLong + sum 实现。
     * 显然，后一种方法无需中间变量, 更直观。
     */
    @Test
    public void map() {
        // 计算所有订单商品数量
        // 通过两次遍历实现
        LongAdder longAdder = new LongAdder();
        orders.stream().forEach(order -> order.getOrderItemList().forEach(orderItem -> longAdder.add(orderItem.getProductQuantity())));
        System.out.println(longAdder.sum());

        // 使用两次 mapToLong + sum 实现
        /**
         * 双重遍历简写
         * long sum = 0L;
         * for (Order order : orders) {
         *      long l = 0L;
         *      for (OrderItem orderItem : order.getOrderItemList()) {
         *          long productQuantity = orderItem.getProductQuantity();
         *          l += productQuantity;
         *       }
         *       sum += l;
         *  }
         */
        long sum = orders.stream().mapToLong(order -> order.getOrderItemList().stream()
                .mapToLong(OrderItem::getProductQuantity)
                .sum()).sum();
        System.out.println(sum);


        /**
         *  再补充一下, 使用 for 循环生成数据, 是我们平时常用的操作。
         *  现在，我们可以用一行代码 IntStream 配合 mapToObj 替代 for 循环来生成数据，比如生成 10 个 product 元素构成 List。
         */
        // 把 IntStream 通过转换为 Stream<Project>
        List<Product> products = IntStream.rangeClosed(1, 10)
                .mapToObj(i -> new Product((long) i, "product" + i, i * 100.0))
                .collect(Collectors.toList());

        // 原始的遍历写法，如下所示：
        List<Product> productList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Product product = new Product((long) i, "product" + i, i * 100.0);
            productList.add(product);
        }
    }

    /**
     * 3、flatMap 方法。
     * 使用：flatMap 展开或者扁平化操作, 相当于 map + flat, 通过 map 把每一个元素替换为一个流, 然后展开这个流。
     * <p>
     * 比如：我们要统计所有订单的总价格, 可以有两种方式。
     */
    @Test
    public void flatMap() {
        // 不依赖订单上的总价格字段
        double sum1 = orders.stream().mapToDouble(order -> order.getTotalPrice()).sum();
        System.out.println(sum1);

        /**
         * 第一种方式: 直接通过原始商品列表的商品个数 * 商品单价，进行统计的话, 可以先把订单通过 flatMap 展开成商品清单,
         * 也就是把 Order 替换为 Stream, 然后对每一个 OrderItem 用 mapToDouble 转换获得商品总价, 最后进行一次 Sum 求和。
         */
        // 直接展开订单商品进行价格统计
        double sum2 = orders.stream()
                .flatMap(order -> order.getOrderItemList().stream())
                .mapToDouble(item -> item.getProductQuantity() * item.getProductPrice())
                .sum();
        System.out.println(sum2);

        /**
         * 第二种方式: 利用 flatMapToDouble 方法把列表中每一项展开替换为一个 DoubleStream,
         * 也就是直接把每一个订单转换为每一个商品的总价，然后求和。
         */
        // 另一种方式是：flatMap + mapToDouble = flatMapToDouble
        double sum3 = orders.stream()
                .flatMapToDouble(order -> order.getOrderItemList().stream()
                        .mapToDouble(item -> item.getProductQuantity() * item.getProductPrice()))
                .sum();
        System.out.println(sum3);
    }

    /**
     * 4、sorted 方法使用
     * sorted 操作可用用于行内排序的场景, 类似 SQL 中的 order by。 比如：要实现大于 50 元订单的按价格倒序取前5,
     * 可以通过 Order::getTotalPrice 方法引用直接指定需要排序的依据字段，通过 reversed() 实现倒序。
     */
    @Test
    public void sorted() {
        // 大于 50 的订单，按照订单价格倒序前 5
        System.out.println("大于 50 的订单，按照订单价格倒序前 5");
        orders.stream().filter(order -> order.getTotalPrice() > 50)  // 过滤大于 50 的订单
                .sorted(Comparator.comparing(Order::getTotalPrice).reversed()) // 按照订单价格倒序排列
                .limit(5) // 取其前 5 个
                .forEach(System.out::println);
    }

    /**
     * 5、distinct 方法
     * distinct 操作的作用是去重。类似 SQL 中的 distinct, 比如下面的代码实现：
     * （1）查询去重后的下单用户。使用 map 从订单提取出购买用户, 然后使用 distinct 去重。
     * （2）查询购买过的商品名。使用 flatMap + map 提取出订单中所有的商品名, 然后使用 distinct 去重。
     */
    @Test
    public void distinct() {
        /**
         * 1、去重下单用户: 使用 map 从订单提取出购买用户, 然后使用 distinct 去重。
         */
        String users = orders.stream()
                .map(order -> order.getCustomerName())
                .distinct()
                .collect(Collectors.joining(","));
        System.out.println(users);

        /**
         * 2、查询购买够的商品: 使用 flatMap + map 提取出订单中所有的商品名, 然后使用 distinct 去重。
         */
        String products = orders.stream()
                .flatMap(order -> order.getOrderItemList().stream())
                .map(OrderItem::getProductName)
                .distinct()
                .collect(Collectors.joining(","));
        System.out.println(products);
    }

    /**
     * 6、skip & limit 方法
     * skip 和 limit 操作用于分页, 类似 MySQL 中的 limit。
     * 其中, skip 实现跳过一定的项, limit 用于限制项总数。
     * <p>
     * 比如下面的代码:
     * (1) 按照下单时间顺序, 查询前 2 个订单的顾客姓名和下单时间。
     * (2) 按照下单时间顺序, 查询第 3 个和第 4 个订单的顾客姓名和下单时间。
     */
    @Test
    public void skipAndLimit() {

        /**
         * (1) 按照下单时间顺序, 查询前 2 个订单的顾客姓名和下单时间。
         */
        orders.stream()
                .sorted(Comparator.comparing(Order::getPlaceAt))
                .map(order -> order.getCustomerName() + "@" + order.getPlaceAt())
                .limit(2)
                .forEach(System.out::println);

        /**
         * (2) 按照下单时间顺序, 查询第 3 个和第 4 个订单的顾客姓名和下单时间。
         */
        orders.stream()
                .sorted(Comparator.comparing(Order::getPlaceAt))
                .map(order -> order.getCustomerName() + "@" + order.getPlaceAt())
                .skip(2)
                .limit(2)
                .forEach(System.out::println);
    }
}

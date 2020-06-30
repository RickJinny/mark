package com.rickjinny.mark.controller.p32_java8;

import com.rickjinny.mark.controller.p32_java8.bean.Customer;
import com.rickjinny.mark.controller.p32_java8.bean.Order;
import com.rickjinny.mark.controller.p32_java8.bean.OrderItem;
import com.rickjinny.mark.controller.p32_java8.bean.Product;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
 * 7、collect 收集操作
 * 8、groupBy 方法
 * 9、partitionBy 方法
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


    /**
     * 7、collect 收集操作
     * 对流进行终止操作, 把流导出为我们需要的数据结构。
     * 终结是指导出后, 无法再串联使用其他的中间操作, 比如: filter、map、flatMap、sorted、distinct、limit、skip。
     * 在 Stream 操作中, collect 是最复杂的终结操作, 比较简单的终结操作还有 forEach、toArray、min、max、count、anyMatch 等。
     * 下面通过 6 个案例:
     */
    @Test
    public void collect() {
        /**
         * 第 1 个案例: 实现了字符串拼接操作, 生成一定位数的随机字符串。
         */
        String str1 = random.ints(48, 122)
                .filter(i -> (i < 57 || i > 65) && (i < 90 || i > 92))
                .mapToObj(i -> (char) i)
                .limit(20)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
        System.out.println(str1);


        /**
         * 第二个案例: 所有下单用户, 使用 toSet 去重后实现字符串拼接
         */
        String str2 = orders.stream()
                .map(order -> order.getCustomerName())
                .collect(Collectors.toSet())
                .stream()
                .collect(Collectors.joining(",", "[", "]"));
        System.out.println(str2);


        /**
         * 第三个案例：用 toCollection 收集器指定集合类型
         */
        LinkedList<Order> linkedList = orders.stream()
                .limit(2)
                .collect(Collectors.toCollection(LinkedList::new));

        /**
         * 第四个案例：使用 toMap 获取订单id + 下单用户名的 Map
         */
        orders.stream()
                .collect(Collectors.toMap(Order::getId, Order::getCustomerName))
                .entrySet()
                .forEach(System.out::println);

        /**
         * 第五个案例：使用 toMap 获取下单用户名 + 最近一次下单时间的 Map
         */
        orders.stream()
                .collect(Collectors.toMap(Order::getCustomerName, Order::getPlaceAt, (x, y) -> x.isAfter(y) ? x : y))
                .entrySet()
                .forEach(System.out::println);

        /**
         * 第六个案例：订单平均购买的商品数量
         */
        Double collect = orders.stream()
                .collect(Collectors.averagingInt(order ->
                        order.getOrderItemList().stream().collect(Collectors.summingInt(OrderItem::getProductQuantity))));
        System.out.println(collect);
    }

    /**
     * 8、groupBy 的用法
     * groupBy 是分组统计操作, 类似 SQL 中的 groupBy 子句。它和后面介绍的 partitioningBy 都是特殊的收集器，同样也是终结操作。
     * 分组操作比较复杂, 为帮你理解得更加透彻, 准备了 8 个案例.
     * 第一个案例：按照用户名分组, 使用 Collectors.counting 方法统计每个人的下单数量, 再按照下单数量倒序输出。
     * 第二个案例：按照用户名分组, 使用 Collectors.summingDouble 方法统计订单总金额，再按照总金额倒序输出。
     * 第三个案例：按照用户名分组, 使用两次 Collectors.summingInt 方法统计商品采购数量，再按总数量倒序输出。
     * 第四个案例：统计被采购最多的商品，先通过 flatMap 把订单转换为商品, 然后把商品名作为 key, Collectors.summingInt 作为 value 分组
     *           统计采购数量, 再按 value 倒序获取第一个 Entry， 最后查询 key 就得到了售出最多的商品。
     * 第五个案例：同样统计采购最多的商品。相比第四个案例排序 Map 的方式, 这次直接使用 Collectors.maxBy 收集器获得最大的 Entry。
     * 第六个案例：按照用户名分组，统计用户下的金额最高的订单。 key 是用户名, value 是 Order, 直接通过 Collectors.maxBy 方法拿到金额最高的
     *           订单，然后通过 collectingAndThen 实现 Optional.get 的内容提取，最后遍历 key / value 即可。
     * 第七个案例：根据下单年月分组统计订单 id 类列表。key 是格式化成年月后的下单时间， value 直接通过 Collectors.mapping 方法进行了转换，
     *           把订单列表转换为订单 id 构成 List。
     * 第八个案例：根据下单年月 + 用户名两次分组统计订单 id 列表, 相比上一个案例多了一次分组操作，第二次分组是按照用户进行分组。
     */
    @Test
    public void groupBy() {
        // 按照用户名分组, 统计下单数量
        System.out.println("照用户名分组, 统计下单数量");
        List<Map.Entry<String, Long>> collect1 = orders.stream()
                .collect(Collectors.groupingBy(Order::getCustomerName, Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue())
                .collect(Collectors.toList());
        System.out.println(collect1);


        // 按照用户名分组, 统计订单总金额
        System.out.println("按照用户分组, 统计订单总金额");
        List<Map.Entry<String, Double>> collect2 = orders.stream()
                .collect(Collectors.groupingBy(Order::getCustomerName, Collectors.summingDouble(Order::getTotalPrice)))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .collect(Collectors.toList());
        System.out.println(collect2);


        // 按照用户名分组, 统计商品采购数量
        System.out.println("按照用户名分组, 统计商品采购数量");
        List<Map.Entry<String, Integer>> collect3 = orders.stream()
                .collect(Collectors.groupingBy(Order::getCustomerName,
                        Collectors.summingInt(order -> order.getOrderItemList().stream()
                                .collect(Collectors.summingInt(OrderItem::getProductQuantity)))))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toList());
        System.out.println(collect3);


        // 统计最受欢迎的商品, 倒序后取第一个
        System.out.println("统计最受欢迎的商品, 倒序后取第一个");
        orders.stream()
                .flatMap(order -> order.getOrderItemList().stream())
                .collect(Collectors.groupingBy(OrderItem::getProductName, Collectors.summingInt(OrderItem::getProductQuantity)))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .findFirst()
                .ifPresent(System.out::println);

        // 统计最受欢迎的商品的另一种方式, 直接利用 maxBy
        orders.stream()
                .flatMap(order -> order.getOrderItemList().stream())
                .collect(Collectors.groupingBy(OrderItem::getProductName, Collectors.summingInt(OrderItem::getProductQuantity)))
                .entrySet()
                .stream()
                .collect(Collectors.maxBy(Map.Entry.comparingByValue()))
                .map(Map.Entry::getKey)
                .ifPresent(System.out::println);

        // 按照用户名分组，选用户下的总金额最大的订单
        System.out.println(" 按照用户名分组，选用户下的总金额最大的订单");
        orders.stream()
                .collect(Collectors.groupingBy(Order::getCustomerName,
                        Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparingDouble(Order::getTotalPrice)), Optional::get)))
                .forEach((k, v) -> System.out.println(k + "#" + v.getTotalPrice() + "@" + v.getPlaceAt()));

        // 根据下单年月分组，统计订单 id 列表
        System.out.println("根据下单年月分组，统计订单 id 列表");
        Map<String, List<Long>> map = orders.stream()
                .collect(Collectors.groupingBy(order -> order.getPlaceAt().format(DateTimeFormatter.ofPattern("yyyyMM")),
                        Collectors.mapping(order -> order.getId(), Collectors.toList())));

        // 根据下单年月 + 用户名两次分组, 统计订单 id 列表
        System.out.println("根据下单年月 + 用户名两次分组, 统计订单 id 列表");
        orders.stream().collect(
                Collectors.groupingBy(order -> order.getPlaceAt().format(DateTimeFormatter.ofPattern("yyyyMM")),
                        Collectors.groupingBy(order -> order.getCustomerName(),
                                Collectors.mapping(order -> order.getId(), Collectors.toList()))));
    }


    /**
     * 9、partitionBy 方法
     * partitionBy 用于分区, 分区是特殊的分组, 只有 true 和 false 两组。
     * 比如：我们把用户按照是否下单进行分区, 给 partitionBy 传入一个 Predicate 作为数据分区的区分, 输出是 Map<Boolean, List<T>
     */
    @Test
    public void partitionBy() {
        // 测试一下，partitionBy 配合 anyMatch，可以把用户分为下过订单和没下过订单两组。
        // 根据是否有下单记录，进行分区
        Map<Boolean, List<Customer>> map = Customer.getData().stream().collect(
                Collectors.partitioningBy(customer -> orders.stream().mapToLong(Order::getCustomerId)
                        .anyMatch(id -> id == customer.getId())));
        System.out.println(map);
    }
}

package com.rickjinny.mark.controller.p10_list.t03_listvsmap;

import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Assert;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 一定要让合适的数据结构做合适的事情。
 * 第一个误区：使用数据结构不考虑平衡时间和空间
 */
public class ListVsMap {

    /**
     * 定义一个包含 elementCount 和 loopCount 两个参数的 listSearch 方法，初始化一个具有 elementCount 个
     * 订单对象的 ArrayList，循环 loopCount 次搜索这个 ArrayList，每次随机搜索一个订单号。
     */
    private static Object listSearch(int elementCount, int loopCount) {
        // 初始化一个具有 elementCount 个订单对象的 ArrayList
        List<Order> list = IntStream.rangeClosed(1, elementCount)
                .mapToObj(i -> new Order(i))
                .collect(Collectors.toList());

        // 循环 loopCount 次搜索这个 ArrayList，每次随机搜索一个订单号
        IntStream.rangeClosed(1, loopCount).forEach(i -> {
            int search = ThreadLocalRandom.current().nextInt(elementCount);
            Order result = list.stream()
                    .filter(order -> order.getOrderId() == search)
                    .findFirst()
                    .orElse(null);
            Assert.assertTrue(result != null && result.getOrderId() == search);
        });
        return list;
    }

    /**
     * 从一个具有 elementCount 个元素的 Map 中循环 loopCount 次查找随机订单号。
     * Map的key是订单号，Value是订单对象。
     *
     * 搜索 ArrayList 的时间复杂度是 O(n), 而 HashMap 的 get 操作的时间复杂度是 O(1)。
     * 所以，要对大 List 进行单值搜索的话，可以考虑使用 HashMap，其中 key 是要搜索的值，value 是原始的对象。
     * 会比使用 ArrayList 有非常明显的性能优势。
     */
    private static Object mapSearch(int elementCount, int loopCount) {
        Map<Integer, Order> map = IntStream.rangeClosed(1, elementCount)
                                        .boxed()
                                        .collect(Collectors.toMap(Function.identity(), i -> new Order(i)));

        IntStream.rangeClosed(1, loopCount).forEach(i -> {
            int search = ThreadLocalRandom.current().nextInt(elementCount);
            Order result = map.get(search);
            Assert.assertTrue(result != null && result.getOrderId() == search);
        });
        return map;
    }



    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class Order {
        private int orderId;
    }

    /**
     * 20861992
     * 72388672
     * StopWatch '': running time = 2469616348 ns
     * ---------------------------------------------
     * ns         %     Task name
     * ---------------------------------------------
     * 2363485008  096%  listSearch
     * 106131340  004%  mapSearch
     *
     * 可以看到，搜索 1000 次，listSearch 方法耗时 2.3 秒；而 mapSearch 耗时仅仅是 106 毫秒。
     */
    public static void main(String[] args) throws InterruptedException {
        int elementCount = 1000000;
        int loopCount = 1000;
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("listSearch");
        Object listSearch = listSearch(elementCount, loopCount);
        System.out.println(ObjectSizeCalculator.getObjectSize(listSearch));
        stopWatch.stop();

        stopWatch.start("mapSearch");
        Object mapSearch = mapSearch(elementCount, loopCount);
        stopWatch.stop();
        System.out.println(ObjectSizeCalculator.getObjectSize(mapSearch));
        System.out.println(stopWatch.prettyPrint());
        TimeUnit.HOURS.sleep(1);
    }
}

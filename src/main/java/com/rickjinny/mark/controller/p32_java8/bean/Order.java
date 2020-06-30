package com.rickjinny.mark.controller.p32_java8.bean;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * 订单类
 */
@Data
public class Order {
    // id
    private Long id;
    // 顾客
    private Long customerId;
    // 顾客姓名
    private String customerName;
    // 订单商品明细
    private List<OrderItem> orderItemList;
    // 总价格
    private Double totalPrice;
    // 下单时间
    private LocalDateTime placeAt;

    public static List<Order> getData() {
        List<Product> products = Product.getData();
        List<Customer> customers = Customer.getData();
        Random random = new Random();
        return LongStream.rangeClosed(1, 10).mapToObj(i -> {
            Order order = new Order();
            order.setId(i);
            order.setPlaceAt(LocalDateTime.now().minusHours(random.nextInt(24 * 365)));
            order.setOrderItemList(getOrderItemList(products, random));
            order.setTotalPrice(getTotalPrice(order));
            Customer customer = customers.get(random.nextInt(customers.size()));
            order.setCustomerId(customer.getId());
            order.setCustomerName(customer.getName());
            return order;
        }).collect(Collectors.toList());
    }

    private static Double getTotalPrice(Order order) {
        return order.getOrderItemList().stream()
                .mapToDouble(item -> item.getProductPrice() * item.getProductQuantity())
                .sum();
    }

    private static List<OrderItem> getOrderItemList(List<Product> products, Random random) {
        return IntStream.rangeClosed(1, random.ints(1, 1, 8).findFirst().getAsInt())
                .mapToObj(j -> {
                    OrderItem orderItem = new OrderItem();
                    Product product = products.get(random.nextInt(products.size()));
                    orderItem.setProductId(product.getId());
                    orderItem.setProductName(product.getName());
                    orderItem.setProductPrice(product.getPrice());
                    orderItem.setProductQuantity(random.ints(1, 1, 5).findFirst().getAsInt());
                    return orderItem;
                }).collect(Collectors.toList());
    }
}

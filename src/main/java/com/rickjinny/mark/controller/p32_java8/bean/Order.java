package com.rickjinny.mark.controller.p32_java8.bean;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

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
}

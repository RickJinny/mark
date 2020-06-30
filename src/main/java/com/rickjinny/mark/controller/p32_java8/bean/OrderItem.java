package com.rickjinny.mark.controller.p32_java8.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单商品类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    // 商品 id
    private Long productId;
    // 商品名称
    private String productName;
    // 商品价格
    private Double productPrice;
    // 商品数量
    private Integer productQuantity;
}

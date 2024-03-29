package com.rick.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateOrderRequest {

    // 用户id
    private Long userId;

    // 商品id
    private Integer productId;

    // 商品价格
    private BigDecimal price;

}

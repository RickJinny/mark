package com.rickjinny.mark.controller.p27_clientdata.t01_TrustClientCalculation.bean;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Order {
    // 商品id
    private Long itemId;
    // 商品价格
    private BigDecimal itemPrice;
    // 商品数量
    private Integer quantity;
    // 商品总价
    private BigDecimal itemTotalPrice;
}

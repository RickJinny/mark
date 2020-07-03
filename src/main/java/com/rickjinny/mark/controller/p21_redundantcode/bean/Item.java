package com.rickjinny.mark.controller.p21_redundantcode.bean;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 购物车中的商品
 */
@Data
public class Item {

    // 商品id
    private Long id;

    // 商品数量
    private int quantity;

    // 商品单价
    private BigDecimal price;

    // 商品优惠
    private BigDecimal couponPrice;

    // 商品运费
    private BigDecimal deliveryPrice;

}

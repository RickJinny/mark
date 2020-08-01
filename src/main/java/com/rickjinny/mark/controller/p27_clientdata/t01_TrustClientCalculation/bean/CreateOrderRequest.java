package com.rickjinny.mark.controller.p27_clientdata.t01_TrustClientCalculation.bean;

import lombok.Data;

@Data
public class CreateOrderRequest {
    // 商品id
    private Long itemId;
    // 商品数量
    private int quantity;
}

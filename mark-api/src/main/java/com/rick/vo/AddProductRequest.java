package com.rick.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddProductRequest {

    private Long productId;

    private String productName;

    private BigDecimal productPrice;

    private Integer userId;

}

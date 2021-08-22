package com.rick.test.dto;

import lombok.Data;

@Data
public class OrderDTO {

    private Long id;

    private Long orderId;

    private String name;

    private Long productId;

    private Double price;

}

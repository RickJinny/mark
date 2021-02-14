package com.rick.test.statemachine;

import lombok.Data;

@Data
public class Order {

    private String orderId;

    private OrderStatus status;

}

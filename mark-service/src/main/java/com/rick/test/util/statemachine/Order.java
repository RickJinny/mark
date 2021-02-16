package com.rick.test.util.statemachine;

import lombok.Data;

@Data
public class Order {

    private String orderId;

    private OrderStatus status;

}

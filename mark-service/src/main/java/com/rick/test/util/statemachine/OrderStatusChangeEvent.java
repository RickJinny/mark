package com.rick.test.util.statemachine;

/**
 * 订单状态改变事件
 */
public enum OrderStatusChangeEvent {

    PAYED, // 支付

    DELIVERY, // 发货

    RECEIVED; // 确认收货

}

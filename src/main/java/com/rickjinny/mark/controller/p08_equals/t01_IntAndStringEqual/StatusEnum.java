package com.rickjinny.mark.controller.p08_equals.t01_IntAndStringEqual;

/**
 * 订单状态枚举
 * 这里使用了 Integer 类型，但是 Integer 类型已经超过了 127 了。
 */
public enum StatusEnum {

    CREATE(1000, "已创建"),
    PAID(1001, "已支付"),
    DELIVERED(1002, "已送到"),
    FINISHED(1003, "已完成");

    public Integer status;

    public String desc;

    StatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}

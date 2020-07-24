package com.rickjinny.mark.controller.p15_serialization.t04_EnumUsedInApi;

import lombok.Getter;

/**
 * 第一个坑是：客户端和服务端的枚举定义不一致时，会出异常。
 */
@Getter
public enum StatusEnumClient {
    CREATE(1, "已创建"),
    PAID(2, "已支付"),
    DELIVERED(3, "已送达"),
    FINISHED(4, "已完成");

    private final int status;
    private final String desc;

    StatusEnumClient(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}

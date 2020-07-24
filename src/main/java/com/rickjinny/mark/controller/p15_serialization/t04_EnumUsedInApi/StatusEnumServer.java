package com.rickjinny.mark.controller.p15_serialization.t04_EnumUsedInApi;

import lombok.Getter;

@Getter
public enum StatusEnumServer {
    CREATE(1, "已创建"),
    PAID(2, "已支付"),
    DELIVERED(3, "已送到"),
    FINISHED(4, "已完成"),
    CANCELED(5, "已取消");

    private final int status;
    private final String desc;

    StatusEnumServer(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}

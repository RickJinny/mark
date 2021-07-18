package com.rick.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class GetOrderDetailResponse {

    private Long id;

    private Long orderId;

    private Integer productId;

    private Long userId;

    private BigDecimal price;

    private Integer status;

    private Date createTime;

    private Date updateTime;

}

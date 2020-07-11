package com.rickjinny.mark.controller.p24_productionready.metrics.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class Order implements Serializable {
    private Long id;
    private Long userId;
    private Long merchantId;
}

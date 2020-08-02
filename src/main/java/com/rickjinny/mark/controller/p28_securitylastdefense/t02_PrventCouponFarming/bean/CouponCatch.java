package com.rickjinny.mark.controller.p28_securitylastdefense.t02_PrventCouponFarming.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class CouponCatch {
    private Long id;
    private AtomicInteger totalCount;
    private AtomicInteger remainCount;
    private BigDecimal amount;
    private String reason;
}

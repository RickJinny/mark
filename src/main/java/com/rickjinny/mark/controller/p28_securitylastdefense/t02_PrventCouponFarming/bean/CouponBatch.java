package com.rickjinny.mark.controller.p28_securitylastdefense.t02_PrventCouponFarming.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 优惠券批次，包含了固定张数的优惠券、申请原因等。
 * 在业务需要发放优惠券时，先申请批次，然后再通过批次发放优惠券。
 */
@Data
public class CouponBatch {
    private Long id;
    private AtomicInteger totalCount;
    private AtomicInteger remainCount;
    private BigDecimal amount;
    private String reason;
}

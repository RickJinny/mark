package com.rickjinny.mark.controller.p28_securitylastdefense.t02_PrventCouponFarming.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Coupon {
    private Long userId;
    private BigDecimal amount;
}

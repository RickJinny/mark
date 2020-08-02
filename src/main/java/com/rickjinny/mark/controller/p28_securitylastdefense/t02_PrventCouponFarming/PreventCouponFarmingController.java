package com.rickjinny.mark.controller.p28_securitylastdefense.t02_PrventCouponFarming;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.stream.IntStream;

@Slf4j
@RestController
@RequestMapping(value = "/preventCouponFarming")
public class PreventCouponFarmingController {

    @RequestMapping(value = "/wrong")
    public int wrong() {
        CouponCenter couponCenter = new CouponCenter();
        // 发送 10000 个优惠券
        IntStream.rangeClosed(1, 10000).forEach(i -> {
            couponCenter.generateCouponWrong(1L, new BigDecimal("100"));
        });
        return couponCenter.getTotalSentCoupon();
    }
}

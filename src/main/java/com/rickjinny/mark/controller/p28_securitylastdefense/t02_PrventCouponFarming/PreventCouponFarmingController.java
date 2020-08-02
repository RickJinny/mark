package com.rickjinny.mark.controller.p28_securitylastdefense.t02_PrventCouponFarming;

import com.rickjinny.mark.controller.p28_securitylastdefense.t02_PrventCouponFarming.bean.Coupon;
import com.rickjinny.mark.controller.p28_securitylastdefense.t02_PrventCouponFarming.bean.CouponBatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.stream.IntStream;

@Slf4j
@RestController
@RequestMapping(value = "/preventCouponFarming")
public class PreventCouponFarmingController {

    /**
     * 错误的发放优惠券，想发多少就发多少。
     */
    @RequestMapping(value = "/wrong")
    public int wrong() {
        CouponCenter couponCenter = new CouponCenter();
        // 发送 10000 个优惠券
        IntStream.rangeClosed(1, 10000).forEach(i -> {
            couponCenter.generateCouponWrong(1L, new BigDecimal("100"));
        });
        return couponCenter.getTotalSentCoupon();
    }

    /**
     * 正确的发放优惠券的方式：通过调用 generateCouponRight 方法，进行发放优惠券，每发一次都会从批次中扣除一张优惠券，发完就没有了。
     * 在真实生产中，要根据 BatchCoupon 在数据库中出入 Coupon 记录，每一个优惠券都有唯一的id，可追踪，可注销。
     */
    @RequestMapping(value = "/right")
    public int right() {
        CouponCenter couponCenter = new CouponCenter();
        // 申请批次
        CouponBatch couponBatch = couponCenter.generateCouponBatch();
        IntStream.rangeClosed(1, 10000).forEach(i -> {
            Coupon coupon = couponCenter.generateCouponRight(1L, couponBatch);
            // 发放优惠券
            couponCenter.sendCoupon(coupon);
        });
        return couponCenter.getTotalSentCoupon();
    }
}

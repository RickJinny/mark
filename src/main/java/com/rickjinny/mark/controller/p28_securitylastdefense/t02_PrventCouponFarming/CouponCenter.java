package com.rickjinny.mark.controller.p28_securitylastdefense.t02_PrventCouponFarming;

import com.rickjinny.mark.controller.p28_securitylastdefense.t02_PrventCouponFarming.bean.Coupon;
import com.rickjinny.mark.controller.p28_securitylastdefense.t02_PrventCouponFarming.bean.CouponBatch;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class CouponCenter {

    // 用于统计发了多少优惠券
    private AtomicInteger totalSent = new AtomicInteger(0);

    public void sendCoupon(Coupon coupon) {
        if (coupon != null) {
            totalSent.incrementAndGet();
        }
    }

    public int getTotalSentCoupon() {
        return totalSent.get();
    }

    /**
     * 没有任何限制，来多少请求生成多少优惠券
     */
    public Coupon generateCouponWrong(Long userId, BigDecimal amount) {
        return new Coupon(userId, amount);
    }

    /**
     * 发放优惠券方法，每发一次都会从批次中扣除一张优惠券，发完了就没有了。
     */
    public Coupon generateCouponRight(Long userId, CouponBatch couponBatch) {
        if (couponBatch.getRemainCount().decrementAndGet() >= 0) {
            return new Coupon(userId, couponBatch.getAmount());
        } else {
            log.info("优惠券批次 {} 剩余优惠券不足", couponBatch.getId());
            return null;
        }
    }

    /**
     * 申请优惠券批次
     */
    public CouponBatch generateCouponBatch() {
        CouponBatch couponBatch = new CouponBatch();
        couponBatch.setAmount(new BigDecimal("100"));
        couponBatch.setId(1L);
        couponBatch.setTotalCount(new AtomicInteger(100));
        couponBatch.setRemainCount(couponBatch.getTotalCount());
        couponBatch.setReason("XXX活动");
        return couponBatch;
    }
}

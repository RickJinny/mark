package com.rickjinny.mark.controller.p21_redundantcode.templatemethod.right;

import com.rickjinny.mark.controller.p21_redundantcode.templatemethod.bean.DB;
import com.rickjinny.mark.controller.p21_redundantcode.templatemethod.bean.Item;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * VIP 用户的购物车 VipUserCart, 直接继承 NormalUserCart, 只需要修改多买优惠策略。
 */
@Service(value = "VipUserCart")
public class VipUserCart extends NormalUserCart {

    @Override
    protected void processCouponPrice(Long userId, Item item) {
        if (item.getQuantity() > 2) {
            item.setCouponPrice(item.getPrice()
                    .multiply(BigDecimal.valueOf(100 - DB.getUserCouponPercent(userId))
                            .divide(new BigDecimal("100")))
                    .multiply(BigDecimal.valueOf(item.getQuantity() - 2)));
        } else {
            item.setCouponPrice(BigDecimal.ZERO);
        }
    }
}

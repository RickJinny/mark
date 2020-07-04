package com.rickjinny.mark.controller.p21_redundantcode.templatemethod.right;

import com.rickjinny.mark.controller.p21_redundantcode.templatemethod.bean.Item;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 内部用户购物车 InternalUserCart 是最简单的，直接设置 0 运费和 0 折扣即可。
 */
@Service(value = "InternalUserCart")
public class InternalUserCart extends AbstractCart {

    @Override
    protected void processCouponPrice(Long userId, Item item) {
        item.setCouponPrice(BigDecimal.ZERO);
    }

    @Override
    protected void processDeliveryPrice(Long userId, Item item) {
        item.setDeliveryPrice(BigDecimal.ZERO);
    }
}

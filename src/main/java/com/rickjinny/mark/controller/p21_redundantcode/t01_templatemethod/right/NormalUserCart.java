package com.rickjinny.mark.controller.p21_redundantcode.t01_templatemethod.right;

import com.rickjinny.mark.controller.p21_redundantcode.t01_templatemethod.bean.Item;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 普通用户的购物车 NormalUserCart，实现的是 0 优惠和 10% 运费的逻辑。
 */
@Service(value = "NormalUserCart")
public class NormalUserCart extends AbstractCart {

    @Override
    protected void processCouponPrice(Long userId, Item item) {
        item.setCouponPrice(BigDecimal.ZERO);
    }

    @Override
    protected void processDeliveryPrice(Long userId, Item item) {
        item.setDeliveryPrice(item.getPrice()
                .multiply(BigDecimal.valueOf(item.getQuantity()))
                .multiply(new BigDecimal("0.1")));
    }
}

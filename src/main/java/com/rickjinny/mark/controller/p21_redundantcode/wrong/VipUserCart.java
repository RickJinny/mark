package com.rickjinny.mark.controller.p21_redundantcode.wrong;

import com.rickjinny.mark.controller.p21_redundantcode.bean.Cart;
import com.rickjinny.mark.controller.p21_redundantcode.bean.DB;
import com.rickjinny.mark.controller.p21_redundantcode.bean.Item;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * VIP用户的购物车逻辑
 */
public class VipUserCart {

    public Cart process(Long userId, Map<Long, Integer> items) {
        Cart cart = new Cart();
        // 把 Map 的购物车转换为 Item 列表
        List<Item> itemList = new ArrayList<>();
        items.entrySet().stream().forEach(entry -> {
            Item item = new Item();
            item.setId(entry.getKey());
            item.setPrice(DB.getItemPrice(entry.getKey()));
            item.setQuantity(entry.getValue());
            itemList.add(item);
        });
        cart.setItems(itemList);

        // 处理运费和商品优惠
        itemList.stream().forEach(item -> {
            // 运费为商品总价的 10%
            item.setDeliveryPrice(item.getPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()))
                    .multiply(new BigDecimal("0.1")));
            // 购买两件以上相同的商品, 第三件开始享受一定折扣
            if (item.getQuantity() > 2) {
                item.setCouponPrice(item.getPrice()
                        .multiply(BigDecimal.valueOf(100 - DB.getUserCouponPercent(userId))
                                .divide(new BigDecimal("100")))
                        .multiply(BigDecimal.valueOf(item.getQuantity() - 2)));
            } else {
                item.setCouponPrice(BigDecimal.ZERO);
            }
        });

        // 计算商品总价
        cart.setTotalItemPrice(cart.getItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        // 计算运费总价
        cart.setTotalDeliveryPrice(cart.getItems().stream()
                .map(Item::getDeliveryPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        // 计算总优惠
        cart.setTotalDiscount(cart.getItems().stream()
                .map(Item::getCouponPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        // 应付总价 = 商品总价 + 运费总价 - 总优惠
        cart.setPayPrice(cart.getTotalItemPrice()
                .add(cart.getTotalDeliveryPrice())
                .subtract(cart.getTotalDiscount()));
        return cart;
    }
}

package com.rickjinny.mark.controller.p21_redundantcode.templatemethod.wrong;

import com.rickjinny.mark.controller.p21_redundantcode.templatemethod.bean.Cart;
import com.rickjinny.mark.controller.p21_redundantcode.templatemethod.bean.DB;
import com.rickjinny.mark.controller.p21_redundantcode.templatemethod.bean.Item;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 免运费、无折扣的内部用户, 同样只是处理商品折扣和运费时的逻辑差异
 */
public class InternalUserCart {

    public Cart process(Long userId, Map<Long, Integer> items) {
        Cart cart = new Cart();
        List<Item> itemList = new ArrayList<>();
        items.entrySet().stream().forEach(entry -> {
            Item item = new Item();
            item.setId(entry.getKey());
            item.setPrice(DB.getItemPrice(entry.getKey()));
            item.setQuantity(entry.getValue());
            itemList.add(item);
        });
        cart.setItems(itemList);

        itemList.stream().forEach(item -> {
            // 免运费
            item.setDeliveryPrice(BigDecimal.ZERO);
            // 无优惠
            item.setCouponPrice(BigDecimal.ZERO);
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

package com.rickjinny.mark.controller.p21_redundantcode.templatemethod.right;

import com.rickjinny.mark.controller.p21_redundantcode.templatemethod.bean.Cart;
import com.rickjinny.mark.controller.p21_redundantcode.templatemethod.bean.DB;
import com.rickjinny.mark.controller.p21_redundantcode.templatemethod.bean.Item;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractCart {

    /**
     * 处理购物车的大量重复逻辑在父类实现
     */
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
        // 让子类处理每一个商品的优惠
        itemList.stream().forEach(item -> {
            processCouponPrice(userId, item);
            processDeliveryPrice(userId, item);
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

    /**
     * 处理商品优惠的逻辑，留给子类实现
     */
    protected abstract void processCouponPrice(Long userId, Item item);

    /**
     * 处理配送费的逻辑，留给子类实现
     */
    protected abstract void processDeliveryPrice(Long userId, Item item);

}

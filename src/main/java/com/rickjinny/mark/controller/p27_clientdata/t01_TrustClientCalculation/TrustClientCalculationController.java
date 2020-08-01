package com.rickjinny.mark.controller.p27_clientdata.t01_TrustClientCalculation;

import com.rickjinny.mark.controller.p27_clientdata.t01_TrustClientCalculation.bean.Item;
import com.rickjinny.mark.controller.p27_clientdata.t01_TrustClientCalculation.bean.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RequestMapping(value = "/trustClientCalculation")
@Slf4j
@RestController
public class TrustClientCalculationController {

    @PostMapping(value = "/wrongOrder")
    public void wrongOrder(@RequestBody Order order) {
        this.createOrder();
    }

    @PostMapping(value = "/rightOrder")
    public void rightOrder(@RequestBody Order order) {
        // 根据商品id，重新查询商品
        Item item = DBUtils.getItem(order.getItemId());
        // 客户端传入的和服务端查询到的商品单价不匹配的时候，给予友好提示
        if (!item.getItemPrice().equals(order.getItemPrice())) {
            throw new RuntimeException("您选购的商品价格有变换，请重新下单!");
        }
        // 重新设置商品单价
        order.setItemPrice(item.getItemPrice());
        // 重新计算商品总价
        BigDecimal totalPrice = item.getItemPrice().multiply(BigDecimal.valueOf(order.getQuantity()));
        // 客户端传入的和服务端查询到的商品总价不匹配的时候，给予友好的提示
        if (order.getItemTotalPrice().compareTo(totalPrice) != 0) {
            throw new RuntimeException("您选购的商品总价有变化，请重新下单!");
        }
        // 重新设置商品总价
        order.setItemPrice(totalPrice);
        // 创建订单
        createOrder();
    }

    private void createOrder() {

    }
}

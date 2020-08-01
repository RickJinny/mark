package com.rickjinny.mark.controller.p27_clientdata.t01_TrustClientCalculation;

import com.rickjinny.mark.controller.p27_clientdata.t01_TrustClientCalculation.bean.CreateOrderRequest;
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
        this.createOrder(order);
    }

    /**
     * 创建订单：前端传递过来的商品价格和总价的信息，这些信息只能用来呈现和校对。
     * 服务端也一定要重新从数据库来初始化商品的价格，重新计算最终的订单价格。如果不怎么做的话，很可能会被黑客利用，商品总价被恶意修改为比较低的价格。
     */
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
        createOrder(order);
    }

    /**
     * 第二种方法：前端传入需要的信息。定义一个 CreateOrderRequest 的 POJO 类做为接口入参。
     */
    @PostMapping(value = "/rightOrder2")
    public Order rightOrder2(@RequestBody CreateOrderRequest createOrderRequest) {
        // 商品id和商品数量是可信的没有问题，其他数据需要由服务端计算
        Item item = DBUtils.getItem(createOrderRequest.getItemId());
        Order order = new Order();
        order.setItemPrice(item.getItemPrice());
        order.setItemTotalPrice(item.getItemPrice().multiply(BigDecimal.valueOf(createOrderRequest.getQuantity())));
        createOrder(order);
        return order;
    }

    private void createOrder(Order order) {

    }
}

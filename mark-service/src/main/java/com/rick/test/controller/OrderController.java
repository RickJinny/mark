package com.rick.test.controller;

import com.rick.common.ServerResponse;
import com.rick.service.OrderService;
import com.rick.vo.CreateOrderRequest;
import com.rick.vo.CreateOrderResponse;
import com.rick.vo.GetOrderDetailRequest;
import com.rick.vo.GetOrderDetailResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @ResponseBody
    @PostMapping(value = "/createOrder")
    public ServerResponse<CreateOrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
        return orderService.createOrder(request);
    }

    @ResponseBody
    @PostMapping(value = "/getOrderDetail")
    public ServerResponse<GetOrderDetailResponse> getOrderDetail(@RequestBody GetOrderDetailRequest request) {
        return orderService.getOrderDetail(request);
    }
}

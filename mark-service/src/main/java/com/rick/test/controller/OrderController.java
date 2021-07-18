package com.rick.test.controller;

import com.rick.common.ServerResponse;
import com.rick.service.OrderService;
import com.rick.request.CreateOrderRequest;
import com.rick.response.CreateOrderResponse;
import com.rick.request.GetOrderDetailRequest;
import com.rick.response.GetOrderDetailResponse;
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

package com.rick.test.service.impl;

import com.rick.service.OrderService;
import com.rick.common.ServerResponse;
import com.rick.vo.CreateOrderRequest;
import com.rick.vo.CreateOrderResponse;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Override
    public ServerResponse<CreateOrderResponse> createOrder(CreateOrderRequest request) {

        return null;
    }
}

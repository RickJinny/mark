package com.rick.service;

import com.rick.common.ServerResponse;
import com.rick.vo.CreateOrderRequest;
import com.rick.vo.CreateOrderResponse;

public interface OrderService {

    ServerResponse<CreateOrderResponse> createOrder(CreateOrderRequest request);

}

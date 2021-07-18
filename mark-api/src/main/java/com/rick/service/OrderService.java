package com.rick.service;

import com.rick.common.ServerResponse;
import com.rick.request.CreateOrderRequest;
import com.rick.response.CreateOrderResponse;
import com.rick.request.GetOrderDetailRequest;
import com.rick.response.GetOrderDetailResponse;

public interface OrderService {

    ServerResponse<CreateOrderResponse> createOrder(CreateOrderRequest request);

    ServerResponse<GetOrderDetailResponse> getOrderDetail(GetOrderDetailRequest request);

}

package com.rick.service;

import com.rick.common.ServerResponse;
import com.rick.vo.CreateOrderRequest;
import com.rick.vo.CreateOrderResponse;
import com.rick.vo.GetOrderDetailRequest;
import com.rick.vo.GetOrderDetailResponse;

public interface OrderService {

    ServerResponse<CreateOrderResponse> createOrder(CreateOrderRequest request);

    ServerResponse<GetOrderDetailResponse> getOrderDetail(GetOrderDetailRequest request);

}

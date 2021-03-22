package com.rick.test.service.impl;

import com.rick.common.ServerResponse;
import com.rick.service.OrderService;
import com.rick.test.dao.dao.OrderDao;
import com.rick.test.dao.model.Order;
import com.rick.vo.CreateOrderRequest;
import com.rick.vo.CreateOrderResponse;
import com.rick.vo.GetOrderDetailRequest;
import com.rick.vo.GetOrderDetailResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Override
    public ServerResponse<CreateOrderResponse> createOrder(CreateOrderRequest request) {
        Long orderId = orderDao.addOrder(request);
        if (orderId == null) {
            return ServerResponse.createByErrorMessage("创建订单失败");
        }
        return ServerResponse.createBySuccess(CreateOrderResponse.builder().orderId(orderId).build());
    }

    @Override
    public ServerResponse<GetOrderDetailResponse> getOrderDetail(GetOrderDetailRequest request) {
        Order order = orderDao.getOrder(request.getOrderId());
        GetOrderDetailResponse orderDetail = new GetOrderDetailResponse();
        BeanUtils.copyProperties(order, orderDetail);
        return ServerResponse.createBySuccess(orderDetail);
    }
}

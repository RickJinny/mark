package com.rick.test.service.impl;

import com.rick.common.ServerResponse;
import com.rick.service.OrderService;
import com.rick.test.dao.dao.OrderDao;
import com.rick.vo.CreateOrderRequest;
import com.rick.vo.CreateOrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Override
    public ServerResponse<CreateOrderResponse> createOrder(CreateOrderRequest request) {
        int rowCount = orderDao.addOrder(request);
        if (rowCount > 0) {
            return ServerResponse.createBySuccess();
        }
        return null;
    }
}

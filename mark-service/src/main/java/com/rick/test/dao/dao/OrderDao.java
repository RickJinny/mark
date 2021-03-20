package com.rick.test.dao.dao;

import com.rick.test.dao.mapper.OrderMapper;
import com.rick.test.dao.model.Order;
import com.rick.test.util.SnowFlake;
import com.rick.vo.CreateOrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderDao {

    @Autowired
    private OrderMapper orderMapper;

    @Transactional(rollbackFor = Exception.class)
    public int addOrder(CreateOrderRequest orderParam) {
        Order order = new Order();
        order.setUserId(order.getUserId());
        order.setPrice(orderParam.getPrice());
        order.setProductId(orderParam.getProductId());
        order.setOrderId(getOrderId());
        return orderMapper.insert(order);
    }

    public Long getOrderId() {
        SnowFlake idWorker = new SnowFlake(1, 1);
        return idWorker.nextId();
    }
}

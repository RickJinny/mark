package com.rick.test.dao.dao;

import com.rick.test.dao.mapper.OrderMapper;
import com.rick.test.dao.model.Order;
import com.rick.test.dao.model.OrderExample;
import com.rick.test.util.SnowFlake;
import com.rick.request.CreateOrderRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Component
public class OrderDao {

    @Autowired
    private OrderMapper orderMapper;

    @Transactional(rollbackFor = Exception.class)
    public Long addOrder(CreateOrderRequest orderParam) {
        Order order = new Order();
        order.setUserId(order.getUserId());
        order.setPrice(orderParam.getPrice());
        order.setProductId(orderParam.getProductId());
        Long orderId = getOrderId();
        order.setOrderId(orderId);
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
        int rowCount = orderMapper.insertSelective(order);
        if (rowCount > 0) {
            return orderId;
        }
        return null;
    }

    public Order getOrder(Long orderId) {
        OrderExample orderExample = new OrderExample();
        orderExample.createCriteria().andOrderIdEqualTo(orderId);
        List<Order> orderList = orderMapper.selectByExample(orderExample);
        if (CollectionUtils.isEmpty(orderList)) {
            return null;
        }
        return orderList.get(0);
    }

    public Long getOrderId() {
        SnowFlake idWorker = new SnowFlake(1, 1);
        return idWorker.nextId();
    }
}

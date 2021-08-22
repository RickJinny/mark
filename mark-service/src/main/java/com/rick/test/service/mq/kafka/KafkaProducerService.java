package com.rick.test.service.mq.kafka;

import com.rick.test.dto.OrderDTO;

public interface KafkaProducerService {

    void send(OrderDTO order);

    void close();
}

package com.rick.test.service.mq.kafka;

public interface KafkaConsumerService {

    void consumeOrder();

    void close();

}

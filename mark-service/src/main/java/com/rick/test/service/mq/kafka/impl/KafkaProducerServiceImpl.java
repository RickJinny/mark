package com.rick.test.service.mq.kafka.impl;

import com.alibaba.fastjson.JSON;
import com.rick.test.dto.OrderDTO;
import com.rick.test.service.mq.kafka.KafkaProducerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Slf4j
@Service
public class KafkaProducerServiceImpl implements KafkaProducerService {

    private Properties properties;

    private KafkaProducer<String, String> producer;

    private static final String TOPIC = "my_topic_01";

    public KafkaProducerServiceImpl() {
        properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.0.121:9092");
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producer = new KafkaProducer<>(properties);
//        producer.initTransactions();
    }
    
    @Override
    public void send(OrderDTO order) {
        String orderJson = JSON.toJSONString(order);
        try {
            // 开启事务
            // producer.beginTransaction();
            // 向 topic = "my_topic_01" 中, 发送消息
            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(TOPIC, order.getId().toString(), orderJson);
            // 发送消息
            producer.send(producerRecord, (RecordMetadata recordMetadata, Exception exception) -> {
            //  if (exception != null) {
            //     producer.abortTransaction();
            //     throw new KafkaException(exception.getMessage() + ", data: " + producerRecord);
            //  }
            });
            // 提交事务
            // producer.commitTransaction();

        } catch (Exception e) {
            // producer.abortTransaction();
        }

        log.info("------------- json: {} -------------", orderJson);
    }

    @Override
    public void close() {
        producer.close();
    }

}

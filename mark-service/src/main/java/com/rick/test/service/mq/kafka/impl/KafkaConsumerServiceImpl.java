package com.rick.test.service.mq.kafka.impl;

import com.alibaba.fastjson.JSON;
import com.rick.test.dto.OrderDTO;
import com.rick.test.service.mq.kafka.KafkaConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@Slf4j
@Service
public class KafkaConsumerServiceImpl implements KafkaConsumerService {

    private Properties properties;

    private KafkaConsumer<String, String> consumer;

    private static final String TOPIC = "my_topic_01";

    private Map<TopicPartition, OffsetAndMetadata> currentOffset = new HashMap<>();

    private Set<String> orderSet = new HashSet<>();

    private volatile boolean flag = true;

    public KafkaConsumerServiceImpl() {
        properties = new Properties();
        // properties.put("enable.auto.commit", false);
        // properties.put("isolation.level", "read_committed");
        // properties.put("auto.offset.reset", "latest");
        properties.put("group.id", "rick-java");
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.0.121:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        consumer = new KafkaConsumer<>(properties);
    }

    @Override
    public void consumeOrder() {
        consumer.subscribe(Collections.singleton(TOPIC));
        try {
            while (true) {
                // 拉取数据
                ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofSeconds(1));
                for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                    OrderDTO orderDTO = JSON.parseObject(consumerRecord.value(), OrderDTO.class);
                    System.out.println("order = " + orderDTO);

                    deduplicationOrder(orderDTO);
                    currentOffset.put(new TopicPartition(consumerRecord.topic(), consumerRecord.partition()),
                            new OffsetAndMetadata(consumerRecord.offset() + 1, "no metadata"));
                    consumer.commitAsync(currentOffset, new OffsetCommitCallback() {
                        @Override
                        public void onComplete(Map<TopicPartition, OffsetAndMetadata> map, Exception exception) {
                            if (exception != null) {
                                exception.printStackTrace();
                            }
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // currentOffsets
                consumer.commitSync();
            } catch (Exception e) {
                consumer.close();
            }
        }
    }

    @Override
    public void close() {
        if (flag) {
            flag = false;
        }
        consumer.close();
    }

    private void deduplicationOrder(OrderDTO orderDTO) {
        orderSet.add(orderDTO.getId().toString());
    }
}

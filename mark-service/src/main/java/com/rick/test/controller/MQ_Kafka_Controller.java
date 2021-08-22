package com.rick.test.controller;

import com.rick.test.service.mq.kafka.KafkaProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Kafka
 */
@Slf4j
@RestController
@RequestMapping(value = "/mq/kafka")
public class MQ_Kafka_Controller {

    private KafkaProducerService kafkaProducerService;


}

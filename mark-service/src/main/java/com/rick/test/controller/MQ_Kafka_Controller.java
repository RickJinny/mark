package com.rick.test.controller;

import com.rick.common.ServerResponse;
import com.rick.test.dto.OrderDTO;
import com.rick.test.service.mq.kafka.KafkaConsumerService;
import com.rick.test.service.mq.kafka.KafkaProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Kafka
 */
@Slf4j
@RestController
@RequestMapping(value = "/mq/kafka")
public class MQ_Kafka_Controller {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private KafkaConsumerService kafkaConsumerService;

    @ResponseBody
    @PostMapping(value = "/testProduce")
    public ServerResponse<String> testProduce() {
        for (int i = 0; i < 100; i++) {
            kafkaProducerService.send(new OrderDTO((long) (100 + i), (long) (100 * (i + 1)),
                    "产品1", (long) (1000 + i), 8.09D));
            kafkaProducerService.send(new OrderDTO((long) (10000 + i), (long) (10000 * (i + 1)),
                    "产品2", (long) (100000 + i), 18.19D));
        }
        return ServerResponse.createBySuccessMessage("success");
    }


    @ResponseBody
    @PostMapping(value = "/testConsumer")
    public ServerResponse<String> testConsumer() {
        kafkaConsumerService.consumeOrder();
        return ServerResponse.createBySuccessMessage("success");
    }
}

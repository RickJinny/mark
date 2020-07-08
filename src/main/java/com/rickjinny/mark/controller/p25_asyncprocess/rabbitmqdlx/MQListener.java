package com.rickjinny.mark.controller.p25_asyncprocess.rabbitmqdlx;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.support.MessagePropertiesConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class MQListener {

    @Autowired
    private MessagePropertiesConverter messagePropertiesConverter;

    @RabbitListener(queues = Constants.QUEUE)
    public void handler(@Payload Message message, Channel channel) throws IOException {
        String str = new String(message.getBody());
        try {
            log.info("Handler 收到消息：{}", str);
            throw new RuntimeException("处理消息失败");
        } catch (Exception e) {
            Map<String, Object> headers = message.getMessageProperties().getHeaders();
            Long retryCount = getRetryCount(headers);
            if (retryCount < Constants.RETRY_COUNT) {
                log.info("Handler 消费消息: {} 异常，准备重试第 {} 次", str, ++retryCount);
                AMQP.BasicProperties rabbitMQProperties = messagePropertiesConverter.fromMessageProperties(message.getMessageProperties(),
                        "UTF-8");
                rabbitMQProperties.builder().headers(headers);
                channel.basicPublish(Constants.BUFFER_EXCHANGE, Constants.BUFFER_ROUTING_KEY, rabbitMQProperties, message.getBody());
            } else {
                log.info("Handler 消费消息: {} 异常, 已重试 {} 次, 发送到死信队列处理! ", str, Constants.RETRY_COUNT);
                channel.basicPublish(Constants.DEAD_EXCHANGE, Constants.DEAD_ROUTING_KEY, null, message.getBody());
            }
        }
    }

    private Long getRetryCount(Map<String, Object> headers) {
        long retryCount = 0;
        if (null != headers) {
            if (headers.containsKey("x-death")) {
                List<Map<String, Object>> deathList = (List<Map<String, Object>>) headers.get("x-death");
                if (!deathList.isEmpty()) {
                    Map<String, Object> deathEntry = deathList.get(0);
                    retryCount = (Long) deathEntry.get("count");
                }
            }
        }
        return retryCount;
    }
}

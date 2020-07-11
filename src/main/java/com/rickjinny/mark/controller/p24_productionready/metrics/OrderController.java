package com.rickjinny.mark.controller.p24_productionready.metrics;

import com.rickjinny.mark.controller.p24_productionready.metrics.bean.Order;
import io.micrometer.core.instrument.Metrics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 下单操作, 以及商户服务的接口
 */
@Slf4j
@RestController
@RequestMapping(value = "/order")
public class OrderController {
    // 总订单创建数量
    private AtomicLong createOrderCounter = new AtomicLong();

    private RabbitAdmin rabbitAdmin;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @PostConstruct
    public void init() {
        // 注册 createOrder.received 指标, gauge 指标只需要像这样初始化一次, 直接关联到 AtomicLong 引用即可
        Metrics.gauge("createOrder.totalSuccess", createOrderCounter);
    }

    /**
     * 下单接口，提供用户id 和 商户id 作为参数
     */
    @GetMapping("/createOrder")
    public void createOrder(@RequestParam("userId") Long userId, @RequestParam("merchantId") Long merchantId) {
        // 记录一次 createOrder.received 指标, 表示收到下单请求
        Metrics.counter("createOrder.received").increment();
        Instant begin = Instant.now();
        try {
            TimeUnit.MILLISECONDS.sleep(200);
            // 模拟无效用户的情况，id < 10 的为无效用户
            if (userId < 10) {
                throw new RuntimeException("invalid user");
            }
            // 查询商户服务
            Boolean merchantStatus = restTemplate.getForObject("http://localhost:45678/order/getMerchantStatus?merchantId="
                    + merchantId, Boolean.class);
            if (merchantStatus == null || !merchantStatus) {
                throw new RuntimeException("closed merchant");
            }

            Order order = new Order();
            // gauge 指标可以得到自动更新
            order.setId(createOrderCounter.incrementAndGet());
            order.setUserId(userId);
            order.setMerchantId(merchantId);
            // 发送 MQ 消息
            rabbitTemplate.convertAndSend(Constant.EXCHANGE, Constant.ROUTING_KEY, order);
            // 记录一次 createOrder.success 指标, 表示下单成功, 同时提供耗时
            Metrics.timer("createOrder.success").record(Duration.between(begin, Instant.now()));
        } catch (Exception e) {
            log.error("createOrder userId {} failed", userId, e);
            // 记录一次 createOrder.failed 指标，表示下单失败，同时提供耗时，并且以 tag 记录失败原因。
        }
    }

    /**
     * 商户查询接口
     */
    @GetMapping("/getMerchantStatus")
    public boolean getMerchantStatus(@RequestParam("merchantId") Long merchantId) throws InterruptedException {
        // 只有商户id为2的商户，才是营业的
        TimeUnit.MILLISECONDS.sleep(200);
        return merchantId == 2;
    }
}

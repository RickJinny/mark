package com.rickjinny.mark.controller.p24_productionready.metrics;

import com.rickjinny.mark.controller.p24_productionready.metrics.bean.Order;
import io.micrometer.core.instrument.Metrics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 配送服务消息处理程序
 */
@RestController
@Slf4j
@RequestMapping(value = "/deliver")
public class DeliverOrderHandlerController {

    // 配送服务运行状态
    private volatile boolean deliverStatus = true;

    private AtomicLong deliverCounter = new AtomicLong();

    @PostConstruct
    public void init() {
        // 同样注册一个 gauge 指标 deliverOrder.totalSuccess, 代表总的配送单量，只需注册一次即可
        Metrics.gauge("deliverOrder.totalSuccess", deliverCounter);
    }

    /**
     * 通过一个外部接口来改变配送状态，模拟配送服务停工
     */
    @PostMapping("status")
    public void status(@RequestParam("status") boolean status) {
        this.deliverStatus = status;
    }

    /**
     * 监听 MQ 消息
     */
    @RabbitListener(queues = Constant.QUEUE, concurrency = "5")
    public void deliverOrder(Order order) {
        Instant begin = Instant.now();
        // 对 deliverOrder.received 进行递增, 代表收到一次订单信息, counter 类型
        Metrics.counter("deliverOrder.received").increment();
        try {
            if (!deliverStatus) {
                throw new RuntimeException("deliver out of service");
            }
            TimeUnit.MILLISECONDS.sleep(500);
            deliverCounter.incrementAndGet();
            // 配送成功指标 deliverOrder.success, timer 类型
            Metrics.timer("deliverOrder.success")
                    .record(Duration.between(begin, Instant.now()));
        } catch (Exception e) {
            log.error("deliver Order {} order", order, e);
            Metrics.timer("deliverOrder.failed", "reason", e.getMessage())
                    .record(Duration.between(begin, Instant.now()));
        }
    }
}

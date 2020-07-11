package com.rickjinny.mark.controller.p24_productionready.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 实现自定义的 HealthIndicator 类, 用于单一线程池的健康状态。
 *
 * 我们可以传入一个 ThreadPoolExecutor, 通过判断队列剩余容量来确定这个组件的健康状态，
 * 有剩余则返回 up, 否则返回 down, 并把线程池队列的两个重要数据，也就是当前队列元素个数和
 * 剩余量，作为补充信息加入 Health。
 */
public class ThreadPoolHealthIndicator implements HealthIndicator {

    private ThreadPoolExecutor threadPool;

    public ThreadPoolHealthIndicator(ThreadPoolExecutor threadPool) {
        this.threadPool = threadPool;
    }

    @Override
    public Health health() {
        // 补充信息
        Map<String, Integer> detail = new HashMap<>();
        // 队列当前元素个数
        detail.put("queue_size", threadPool.getQueue().size());
        // 队列剩余容量
        detail.put("queue_remaining", threadPool.getQueue().remainingCapacity());
        // 如果还有剩余量, 则返回up, 否则返回down
        if (threadPool.getQueue().remainingCapacity() > 0) {
            return Health.up().withDetails(detail).build();
        } else {
            return Health.down().withDetails(detail).build();
        }
    }
}

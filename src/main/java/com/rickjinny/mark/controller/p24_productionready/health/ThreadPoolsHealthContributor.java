package com.rickjinny.mark.controller.p24_productionready.health;

import org.springframework.boot.actuate.health.CompositeHealthContributor;
import org.springframework.boot.actuate.health.HealthContributor;
import org.springframework.boot.actuate.health.NamedContributor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 定义一个 ThreadPoolsHealthContributor, 来聚合两个 ThreadPoolHealthIndicator
 */
@Component
public class ThreadPoolsHealthContributor implements CompositeHealthContributor {

    // 保存所有的子 HealthContributor
    private Map<String, HealthContributor> contributors = new HashMap<>();

    public ThreadPoolsHealthContributor() {
        // 对应 ThreadPoolProvider 中定义的两个线程组
        contributors.put("demoThreadPool", new ThreadPoolHealthIndicator(ThreadPoolProvider.getDemoThreadPool()));
        contributors.put("ioThreadPool", new ThreadPoolHealthIndicator(ThreadPoolProvider.getIoThreadPool()));
    }

    @Override
    public HealthContributor getContributor(String name) {
        // 根据 name 找到某一个 HealthContributor
        return contributors.get(name);
    }

    @Override
    public Iterator<NamedContributor<HealthContributor>> iterator() {
        // 返回 NamedContributor 的迭代器，NamedContributor 也就是 Contributor 实例 + 一个命名
        return contributors.entrySet().stream()
                .map(entry -> NamedContributor.of(entry.getKey(), entry.getValue())).iterator();
    }
}

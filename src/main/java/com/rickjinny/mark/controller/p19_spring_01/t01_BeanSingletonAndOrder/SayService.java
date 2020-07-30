package com.rickjinny.mark.controller.p19_spring_01.t01_BeanSingletonAndOrder;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 定义一个 SayService 抽象类，其中维护了一个类型是 ArrayList 的字段 data，用于保存方法处理的中间数据。
 * 每次调用 say 方法都会往 data 加入新数据，可以认为 SayService 是有状态的，如果 SayService 是单例的话
 * 必然会 OOM。
 */
@Slf4j
public class SayService {

    List<String> data = new ArrayList<>();

    public void say() {
        data.add(IntStream.rangeClosed(1, 1_000_000)
                .mapToObj(__ -> "a")
                .collect(Collectors.joining("")) + UUID.randomUUID().toString());
        log.info("I am {} size: {}", this, data.size());
    }
}

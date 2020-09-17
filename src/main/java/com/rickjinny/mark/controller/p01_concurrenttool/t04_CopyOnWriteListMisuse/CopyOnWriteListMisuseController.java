package com.rickjinny.mark.controller.p01_concurrenttool.t04_CopyOnWriteListMisuse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@RestController
@RequestMapping(value = "/copyOnWriteListMisuse")
public class CopyOnWriteListMisuseController {

    /**
     * 测试并发写的性能
     */
    @RequestMapping(value = "/testWrite")
    public Map<Object, Object> testWrite() {
        CopyOnWriteArrayList<Object> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
        List<Object> synchronizedList = Collections.synchronizedList(new ArrayList<>());
        StopWatch stopWatch = new StopWatch();
        int loopCount = 100000;

        stopWatch.start("Write: copyOnWriteArrayList");
        // 循环 100000 次，并发往 CopyOnWriteArrayList 写入随机元素
        IntStream.rangeClosed(1, loopCount).parallel().forEach(__ -> copyOnWriteArrayList.add(ThreadLocalRandom.current().nextInt(loopCount)));
        stopWatch.stop();

        stopWatch.start("Write: synchronizedList");
        // 循环 100000 次，并发往加锁的 ArrayList 写入随机元素
        IntStream.rangeClosed(1, loopCount).parallel().forEach(__ -> synchronizedList.add(ThreadLocalRandom.current().nextInt(loopCount)));
        stopWatch.stop();

        log.info(stopWatch.prettyPrint());

        Map<Object, Object> result = new HashMap<>();
        result.put("copyOnWriteArrayList", copyOnWriteArrayList.size());
        result.put("synchronizedList", synchronizedList.size());
        return result;
    }

    private void addAll(List<Integer> list) {
        list.addAll(IntStream.rangeClosed(1, 1000000).boxed().collect(Collectors.toList()));
    }

    /**
     * 测试并发读的性能
     */
    @RequestMapping(value = "/testRead")
    public Map<Object, Object> testRead() {
        // 创建两个对象
        CopyOnWriteArrayList<Integer> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
        List<Integer> synchronizedList = Collections.synchronizedList(new ArrayList<>());
        // 填充数据
        addAll(copyOnWriteArrayList);
        addAll(synchronizedList);

        StopWatch stopWatch = new StopWatch();
        int loopCount = 1000000;
        int count = copyOnWriteArrayList.size();
        stopWatch.start("Read: copyOnWriteArrayList");
        // 循环 1000000 次并发，从 CopyOnWriteArrayList 随机查询元素
        IntStream.rangeClosed(1, loopCount).parallel().forEach(__ -> copyOnWriteArrayList.get(ThreadLocalRandom.current().nextInt(count)));
        stopWatch.stop();

        stopWatch.start("Read: synchronizedList");
        // 循环 1000000 次并发，从加锁的 ArrayList 随机查询元素
        IntStream.rangeClosed(0, loopCount).parallel().forEach(__ -> synchronizedList.get(ThreadLocalRandom.current().nextInt(count)));
        stopWatch.stop();
        log.info(stopWatch.prettyPrint());

        Map<Object, Object> result = new HashMap<>();
        result.put("copyOnWriteArrayList", copyOnWriteArrayList);
        result.put("synchronizedList", synchronizedList);
        return result;
    }
}

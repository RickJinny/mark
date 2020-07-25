package com.rickjinny.mark.controller.p17_oom.t02_WeakHashMapOOM;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;

/**
 * 2、使用 WeakHashMap 不等于不会 OOM
 * 为了防止缓存中堆积大量数据，导致 OOM，一些同学可能会想到使用 WeakHashMap 作为缓存容器。
 * WeakHashMap 的特点是：key 在哈希表内部是弱引用，当没有强引用指向这个 key 之后，Entry 会被 GC，即使我们无限往 WeakHashMap 加入数据，
 * 只要 key 不再使用，也就不会 OOM。
 *
 * 说到了强引用和弱引用，回顾一下 Java 中引用类型和垃圾回收的关系:
 * 第一、垃圾回收器不会回收有强引用的对象;
 * 第二、在内存充足时，垃圾回收器不会回收具有软引用的对象;
 * 第三、垃圾回收器只要扫描了具有弱引用的对象就会回收，WeakHashMap 就是利用了这个特点;
 *
 * 不过，我要和你分享一下，恰巧是不久前遇到使用 WeakHashMap 却最终 OOM 的案例。我们暂且不论使用 WeakHashMap 作为缓存是否合适，先分析一下
 * 这个 OOM 问题。
 */
@RestController
@Slf4j
@RequestMapping(value = "/weakHashMapOOM")
public class WeakHashMapOOMController {

    // 声明一个 key 是 User 类型，value 是 UserProfile 类型的 WeakHashMap，作为用户数据缓存。
    private Map<User, UserProfile> cache = new WeakHashMap<>();

    /**
     * 往 WeakHashMap 类型的 cache 中，添加 200 万个 Entry，然后使用
     * Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate 发起一个定时任务，
     * 每隔 1 秒输出缓存中的 Entry 个数。
     */
    @RequestMapping(value = "/wrong")
    public void wrong() {
        String userName = "xiaoming";
        // 间隔1秒定时输出缓存中的条目数
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() ->
                log.info("cache size: {}", cache.size()), 1, 1, TimeUnit.SECONDS);
        LongStream.rangeClosed(1, 2000000).forEach(i -> {
            User user = new User(userName + i);
            cache.put(user, new UserProfile(user, "location" + i));
        });
    }
}

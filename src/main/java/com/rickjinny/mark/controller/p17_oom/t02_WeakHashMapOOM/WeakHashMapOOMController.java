package com.rickjinny.mark.controller.p17_oom.t02_WeakHashMapOOM;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.ref.WeakReference;
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
     *
     * 输出的结果 cache size 始终是 200 万，即使我们通过 JvisualVM 进行手动 GC 还是这样。
     * 这就说明，这些 Entry 无法通过 GC 回收。如果你把 200万改为 1000 万，就可以在日志中看到 OOM 错误。
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


    /**
     * 在 wrong() 方法中，使用 Map<User, UserProfile> cache = new WeakHashMap<>();
     * WeakHashMap 的 key 虽然是弱引用，但是其 value 却持有 key 中对象的强引用，value 被
     * Entry 引用，Entry 被 WeakHashMap 引用，最终导致 key 无法回收。
     *
     * 第一种解决方案：就是让 value 变为弱引用，使用 WeakReference 来包装 UserProfile 即可。
     */
    private Map<User, WeakReference<UserProfile>> cache2 = new WeakHashMap<>();

    /**
     * 重新运行程序，从日志观察到 cache size 不再是固定的 200万，而是不断减少，甚至在手动 GC 后所有的 Entry 都被回收了。
     */
    @RequestMapping(value = "/right1")
    public void right1() {
        String userName = "haha";
        // 间隔1秒定时输出缓存中的条目数
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
                () -> log.info("cache size : {}", cache2.size()), 1, 1, TimeUnit.SECONDS);

        LongStream.rangeClosed(1, 2000000).forEach(i -> {
            User user = new User(userName + i);
            // 这次，我们使用弱引用来包装 UserProfile
            cache2.put(user, new WeakReference<>(new UserProfile(user, "location" + i)));
        });
    }

    /**
     * 第二种解决方案：让 value 也就是 UserProfile 不再引用 key，而是重新 new 出一个新的 User 对象赋值给 UserProfile。
     */
    @RequestMapping(value = "/right2")
    public void right2() {
        String userName = "haha";

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
                () -> log.info("cahche size : {}", cache.size()), 1, 1, TimeUnit.SECONDS);

        LongStream.rangeClosed(1, 2000000).forEach(i -> {
            User user = new User(userName + i);
            cache.put(user, new UserProfile(new User(user.getName()), "location" + i));
        });
    }

    /**
     * 第三种解决方案：Spring 提供的 ConcurrentReferenceHashMap 类可以使用弱引用、软引用做缓存，key 和 value 同时被
     * 软引用或弱引用包装，也能解决相互引用导致的数据不能释放问题。与 WeakHashMap 相比，ConcurrentReferenceHashMap 不但
     * 性能更好，还可以确保线程安全。
     */
    private Map<User, UserProfile> cache03 = new ConcurrentReferenceHashMap<>();

    @RequestMapping(value = "right3")
    public void right3() {
        String userName = "right3";
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
                () -> log.info("cache size: {}", cache03.size()), 1, 1, TimeUnit.SECONDS);

        LongStream.rangeClosed(1, 20000000).forEach(i -> {
            User user = new User(userName + i);
            cache03.put(user, new UserProfile(user, "location" + i));
        });
    }
}

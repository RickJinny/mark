package com.rickjinny.mark.controller.p02;

import com.rickjinny.mark.bean.Data;
import com.rickjinny.mark.bean.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/lock")
@Slf4j
public class T02_ThreadLockController {

    /**
     * 1、synchronized 加锁的问题
     */
    private volatile int a = 1;
    private volatile int b = 1;

    public synchronized void add() {
        log.info("add start");
        for (int i = 0; i < 10000; i++) {
            a++;
            b++;
        }
        log.info("add done");
    }

    public synchronized void compare() {
        log.info("compare start");
        for (int i = 0; i < 10000; i++) {
            // a 始终等于 b 吗？
            if (a < b) {
                log.info("a:{}, b:{}, {}", a, b, a < b);
                // 最后的 a > b 应该始终是 false 的吗？
            }
        }
        log.info("compare done");
    }

    public static void main(String[] args) {
        T02_ThreadLockController lock = new T02_ThreadLockController();
        new Thread(() -> lock.add()).start();
        new Thread(() -> lock.compare()).start();
    }




    /**
     * 2、加锁前要清楚锁和被保护的对象是不是一个层面的
     *    Data 的 getCounter() 方法，要使用对象锁，不要使用方法锁
     */
    @RequestMapping("/wrong")
    public int wrong(@RequestParam(value = "count", defaultValue = "1000000") int counter) {
        Data.reset();
        // 多线程循环，一定次数调用不同实例的 wrong 方法
        IntStream.rangeClosed(1, counter).parallel()
                .forEach(i -> new Data().wrong());
        return Data.getCounter();
    }




    /**
     * 3、精细化加锁，只对 List<Integer> 对象加锁
     */
    private List<Integer> data = new ArrayList<>();

    // 不涉及共享资源的慢方法
    private void slow() {
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 错误的加锁方法
    @RequestMapping("/wrong3")
    public int wrong3() {
        long begin = System.currentTimeMillis();
        IntStream.rangeClosed(1, 1000).parallel().forEach(i -> {
            // 加锁粒度太粗了，show 执行很慢，所以会导致很慢
            synchronized (this) {
                slow();
                data.add(i);
            }
        });
        log.info("took:{}", System.currentTimeMillis() - begin);
        return data.size();
    }

    // 正确的加锁方法
    @RequestMapping("/right3")
    public int right3() {
        long begin = System.currentTimeMillis();
        IntStream.rangeClosed(1,1000).parallel().forEach(i -> {
            slow();
            // 只对 List 加锁, 不要对 slow() 方法加锁
            synchronized (data) {
                data.add(i);
            }
        });
        log.info("took:{}", System.currentTimeMillis() - begin);
        return data.size();
    }


    /**
     * 4、多把锁要小心死锁问题
     */

    private boolean createOrder(List<Product> order) {
        // 存放所有获得的锁
        List<ReentrantLock> locks = new ArrayList<>();

        for (Product product : order) {
            try {
                // 获得锁 10 秒，超时
                if (product.lock.tryLock(10, TimeUnit.SECONDS)) {
                    locks.add(product.lock);
                } else {
                    locks.forEach(ReentrantLock::unlock);
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 锁全部拿到之后，执行扣减库存业务逻辑
        try {
            order.forEach(item -> item.remaining--);
        } finally {
            locks.forEach(ReentrantLock::unlock);
        }
        return true;
    }

    private ConcurrentHashMap<String, Product> products = new ConcurrentHashMap<>();

    private List<Product> createCart() {
        return IntStream.rangeClosed(1, 3)
                    .mapToObj(i -> "item" + ThreadLocalRandom.current().nextInt(products.size()))
                    .map(name -> products.get(name))
                    .collect(Collectors.toList());
    }

    @RequestMapping("/wrong4")
    public long wrong4() {
        long begin = System.currentTimeMillis();
        // 并发进行 100 次下单操作，统计成功次数
        long success = IntStream.rangeClosed(1, 100).parallel()
                .mapToObj(i -> {
                    List<Product> cart = createCart();
                    return createOrder(cart);
                })
                .filter(result -> result)
                .count();
        log.info("success:{} totalRemaining:{} took:{}ms items:{}",
                success,
                products.entrySet().stream().map(item -> item.getValue().remaining).reduce(0, Integer::sum),
                System.currentTimeMillis() - begin, products);
        return success;
    }

    @RequestMapping("/right4")
    public long right4() {
        long begin = System.currentTimeMillis();
        long success = IntStream.rangeClosed(1, 100).parallel()
                .mapToObj(i -> {
                    List<Product> cart = createCart().stream()
                            .sorted(Comparator.comparing(Product::getName))
                            .collect(Collectors.toList());
                    return createOrder(cart);
                })
                .filter(result -> result)
                .count();
        log.info("success:{} totalRemaining:{} took:{}ms items:{}",
                success,
                products.entrySet().stream().map(product -> product.getValue().remaining).reduce(0, Integer::sum),
                System.currentTimeMillis() - begin, products);
        return success;
    }
}

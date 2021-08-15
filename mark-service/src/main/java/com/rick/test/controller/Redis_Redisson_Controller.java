package com.rick.test.controller;

import com.alibaba.fastjson.JSON;
import com.rick.common.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.config.Config;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Slf4j
@RestController
@RequestMapping(value = "/redisson")
public class Redis_Redisson_Controller {

    private RedissonClient redissonClient;

    private RedissonReactiveClient redissonReactiveClient;

    private RedissonRxClient redissonRxClient;

    @ResponseBody
    @PostMapping(value = "/testRedisson")
    public ServerResponse<String> testRedisson() throws Exception {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.0.121:6379");
        redissonClient = Redisson.create(config);
        redissonReactiveClient = Redisson.createReactive(config);
        redissonRxClient = Redisson.createRx(config);

        // RList 实现了 List 接口
        list(redissonClient);
        // RMap 实现了 ConcurrentHashMap 接口 和 Map 接口
        map(redissonClient);
        // RSet 实现了 Set 接口
        set(redissonClient);
        // RQueue 实现了 Queue 接口
        queue(redissonClient);
        // 可重入锁 RLock 实现了 Lock 接口
        lock(redissonClient);
        // bucket
        bucketType(redissonClient, redissonReactiveClient, redissonRxClient);
        // 二进制流
        streamType(redissonClient);
        // AtomicLong
        atomicLongType(redissonClient);
        // 限流器
//        rateLimiterType(redissonClient);



        redissonClient.shutdown();
        redissonRxClient.shutdown();
        redissonClient.shutdown();
        return ServerResponse.createBySuccessMessage("success");

    }

    /**
     * 可重入锁: RLock 实现了 Lock 接口
     */
    private void lock(RedissonClient redissonClient) {
        RLock rLock = redissonClient.getLock("lock");
        for (int i = 0; i < 5; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    rLock.lock();
                    try {
                        System.out.println(Thread.currentThread() + "_" + System.currentTimeMillis() + "_" + "获取了锁");
                        Thread.sleep(500);
                    } catch (Exception e) {
                        log.error("error message: {} ", e.getMessage(), e);
                    } finally {
                        rLock.unlock();
                    }
                }
            }).start();
        }
    }

    /**
     * RQueue 实现了 Queue 接口
     */
    private void queue(RedissonClient redissonClient) {
        RQueue<String> rQueue = redissonClient.getQueue("queue");
        rQueue.add("haha_01");
        rQueue.add("haha_02");
        rQueue.add("haha_03");
        log.info("first queue poll: {} ", rQueue.poll());
        log.info("second queue poll: {} ", rQueue.poll());
    }

    /**
     * RSet 实现了 Set 接口
     */
    private void set(RedissonClient redissonClient) {
        RSet<String> rSet = redissonClient.getSet("set");
        rSet.add("aa");
        rSet.add("bb");
        rSet.add("cc");
        rSet.forEach(System.out::println);
    }

    /**
     * RMap 实现了 ConcurrentHashMap 接口 和 Map 接口
     */
    private void map(RedissonClient redissonClient) {
        RMap<String, String> map = redissonClient.getMap("map");
        map.put("name", "wangda");
        map.put("age", "30");
        map.put("sex", "男");
        map.remove("sex");
        map.forEach((key, value) -> System.out.println("key = " + key + " , value=" + value));
    }

    /**
     * RList 实现了 List 接口
     */
    private void list(RedissonClient redissonClient) {
        System.out.println("-------------- Redisson list Type ------------------");
        RList<String> rList = redissonClient.getList("list");
        rList.add("A01");
        rList.add("B01");
        rList.add("C01");
        rList.add("D01");
        rList.remove(1);
        log.info("rList: {} ", JSON.toJSONString(rList));
        System.out.println("-------------- Redisson list Type ------------------");
    }

    /**
     * 限流器
     */
    private void rateLimiterType(RedissonClient redissonClient) throws InterruptedException {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter("rateLimiter");
        // 初始化，最大流速：每 1 秒钟产生 5 个令牌
        rateLimiter.trySetRate(RateType.OVERALL, 5, 1, RateIntervalUnit.SECONDS);
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {

                int count = 0;

                @Override
                public void run() {
                    while (true) {
                        rateLimiter.acquire(1);
                        System.out.println(Thread.currentThread() + "_" + System.currentTimeMillis() + "_" + count++);
                    }
                }
            }).start();
        }

        Thread.sleep(1000 * 5);

    }

    /**
     * AtomicLong
     */
    private void atomicLongType(RedissonClient redissonClient) {
        RAtomicLong atomicLong = redissonClient.getAtomicLong("atomicLong");
        atomicLong.set(10);
        atomicLong.incrementAndGet();
        log.info("atomicLongType atomicLong: {} .", JSON.toJSONString(atomicLong));
    }

    /**
     * 二进制流
     * 提供了 InputStream 接口 和 OutputStream 接口的实现
     */
    private void streamType(RedissonClient redissonClient) throws IOException {
        System.out.println("-------------- Redisson stream Type ------------------");
        RBinaryStream rBinaryStream = redissonClient.getBinaryStream("stream");
        rBinaryStream.set("stream_user01".getBytes());
        OutputStream outputStream = rBinaryStream.getOutputStream();
        outputStream.write("zhangsan".getBytes());

        InputStream inputStream = rBinaryStream.getInputStream();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int len;
        while ((len = inputStream.read(b)) != -1) {
            byteArrayOutputStream.write(b, 0, len);
        }
        log.info("byteArrayOutputStream: {} .", JSON.toJSONString(byteArrayOutputStream));
        System.out.println("-------------- Redisson stream Type ------------------");
    }

    /**
     * 通用对象桶，可以同来存放任何类型的对象
     */
    private void bucketType(RedissonClient client, RedissonReactiveClient reactiveClient, RedissonRxClient rxClient) throws Exception {
        System.out.println("-------------- Redisson bucket Type ------------------");
        // 同步操作
        RBucket<String> rBucket = client.getBucket("user01");
        rBucket.set("xiaowang01");
        log.info("stringType rBucket user01: {}", rBucket.get());

        // 异步
        RBucket<String> rBucket2 = client.getBucket("user02");
        rBucket2.setAsync("xiaowang02").get();
        rBucket2.getAsync().thenAccept(System.out::println);

        // RedissonReactiveClient
        RBucketReactive<String> rBucketReactive = reactiveClient.getBucket("user03");
        rBucketReactive.set("xiaowang03").block();
        rBucketReactive.get().subscribe(System.out::println);

        // RxJava
        RBucketRx<String> rBucketRx = rxClient.getBucket("user04");
        rBucketRx.set("xiaowang04");
        log.info("stringType rBucket user04: {}", rBucketRx.get());

        Thread.sleep(5000);

        System.out.println("-------------- Redisson bucket Type ------------------");
    }

}

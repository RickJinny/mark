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

        // String 类型相关操作
        bucketType(redissonClient, redissonReactiveClient, redissonRxClient);
        // 二进制流
        streamType(redissonClient);
        // AtomicLong
        atomicLongType(redissonClient);




        redissonClient.shutdown();
        redissonRxClient.shutdown();
        redissonClient.shutdown();
        return ServerResponse.createBySuccessMessage("success");

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

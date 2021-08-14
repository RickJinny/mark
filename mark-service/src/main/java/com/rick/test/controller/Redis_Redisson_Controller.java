package com.rick.test.controller;

import com.rick.common.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.config.Config;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/redisson")
public class Redis_Redisson_Controller {

    private RedissonClient redissonClient;

    private RedissonReactiveClient redissonReactiveClient;

    private RedissonRxClient redissonRxClient;

    @ResponseBody
    @PostMapping(value = "/testSync")
    public ServerResponse<String> testRedissonSync() throws Exception {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.0.121:6379");
        redissonClient = Redisson.create(config);
        redissonReactiveClient = Redisson.createReactive(config);
        redissonRxClient = Redisson.createRx(config);
        // String 类型相关操作
        stringType(redissonClient, redissonReactiveClient, redissonRxClient);




        redissonClient.shutdown();
        redissonRxClient.shutdown();
        redissonClient.shutdown();
        return ServerResponse.createBySuccessMessage("success");

    }

    /**
     * String 类型相关操作
     */
    private void stringType(RedissonClient client, RedissonReactiveClient reactiveClient, RedissonRxClient rxClient) throws Exception {
        System.out.println("-------------- Lettuce ZSet Type ------------------");
        // 同步操作
        RBucket<String> rBucket = client.getBucket("user01");
        rBucket.set("xiaowang01");
        log.info("stringType rBucket user: {}", rBucket.get());

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

        Thread.sleep(5000);

        System.out.println("-------------- Lettuce ZSet Type ------------------");
    }

}

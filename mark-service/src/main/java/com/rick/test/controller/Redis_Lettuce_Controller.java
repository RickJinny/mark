package com.rick.test.controller;

import com.alibaba.fastjson.JSON;
import com.rick.common.ServerResponse;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.TransactionResult;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@RestController
@RequestMapping(value = "/lettuce")
public class Redis_Lettuce_Controller {

    @ResponseBody
    @PostMapping(value = "/testSync")
    public ServerResponse<String> testRedisLettuceSync() {
        RedisClient redisClient = RedisClient.create(RedisURI.Builder.redis("192.168.0.121", 6379).build());
        StatefulRedisConnection<String, String> connection = redisClient.connect();

        // 同步操作
        RedisCommands<String, String> syncCommands = connection.sync();
        // 清空
        syncCommands.flushall();
        // String 类型相关操作
        stringType(syncCommands);
        // Hash 类型相关操作
        hashType(syncCommands);
        // List 类型相关操作
        listType(syncCommands);
        // Set 类型相关操作
        setType(syncCommands);
        // ZSet 类型相关操作
        zSetType(syncCommands);

        // 同步操作，模拟抢票
        //  useSyncCommands(syncCommands);

        connection.close();
        redisClient.shutdown();

        return ServerResponse.createBySuccess("success");
    }

    /**
     * ZSet 类型相关操作
     */
    private void zSetType(RedisCommands<String, String> syncCommands) {

    }

    /**
     * Set 类型相关操作
     */
    private void setType(RedisCommands<String, String> syncCommands) {
        System.out.println("-------------- Lettuce Set Type ------------------");
        syncCommands.sadd("lettuce_set01", "111", "222", "333", "444", "555");
        Set<String> lettuceSet01Value = syncCommands.smembers("lettuce_set01");
        log.info("Lettuce Set Type, lettuceSet01Value: {} ", JSON.toJSONString(lettuceSet01Value));
        System.out.println("-------------- Lettuce Set Type ------------------");
    }

    /**
     * List 类型相关操作
     */
    private void listType(RedisCommands<String, String> syncCommands) {
        System.out.println("-------------- Lettuce List Type ------------------");
        syncCommands.lpush("list01", "aaa");
        syncCommands.rpush("list01", "bbb");
        syncCommands.rpush("list01", "ccc");
        String list01Value01 = syncCommands.lpop("list01");
        log.info("Lettuce List Type, list01Value01: {} ", list01Value01);

        syncCommands.lpush("list02", "a1", "a2", "a3");
        syncCommands.rpush("list02", "b1", "b2", "b3");
        List<String> list02 = syncCommands.lrange("list02", 0, 20);
        log.info("Lettuce List Type, list02: {} ", JSON.toJSONString(list02));

        System.out.println("-------------- Lettuce List Type ------------------");
    }

    /**
     * Hash 类型相关操作
     */
    private void hashType(RedisCommands<String, String> syncCommands) {
        System.out.println("-------------- Lettuce Hash Type ------------------");
        syncCommands.hset("lettuce_user01", "name", "zhangsan01");
        syncCommands.hset("lettuce_user01", "age", "26");
        List<String> lettuceUser01Keys = syncCommands.keys("lettuce_user01");
        log.info("Lettuce Hash, lettuce_user01, keys: {} ", JSON.toJSONString(lettuceUser01Keys));
        String lettuceUser01Name = syncCommands.hget("lettuce_user01", "name");
        log.info("Lettuce Hash, lettuce_user01, name: {} ", lettuceUser01Name);
        String lettuceUser01Age = syncCommands.hget("lettuce_user01", "age");
        log.info("Lettuce Hash, lettuce_user01, age: {} ", lettuceUser01Age);
        Map<String, String> map = syncCommands.hgetall("lettuce_user01");
        log.info("Lettuce Hash, lettuce_user01: {} ", JSON.toJSONString(map));

        System.out.println("-------------- Lettuce Hash Type ------------------");
    }

    /**
     * String 类型相关操作
     */
    private void stringType(RedisCommands<String, String> syncCommands) {
        System.out.println("-------------- Lettuce String Type ------------------");
        syncCommands.set("lettuce_key01", "xiaowang");
        String value01 = syncCommands.get("lettuce_key01");
        log.info("lettuce_key01: {}", value01);
        System.out.println("-------------- Lettuce String Type ------------------");
    }

    private void useSyncCommands(RedisCommands<String, String> syncCommands) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10000; i++) {
            executorService.execute(() -> {
                try {
                    String info = UUID.randomUUID().toString().replace("-", "");
                    syncCommands.watch("watch_key");
                    String watchKeyValue = syncCommands.get("watch_key");
                    if (Integer.parseInt(watchKeyValue) < 10) {
                        syncCommands.multi();
                        syncCommands.set("watch_key", String.valueOf(Integer.parseInt(watchKeyValue) + 1));
                        TransactionResult result = syncCommands.exec();
                        if (result != null) {
                            log.info("{}, 抢购成功", info);
                            syncCommands.sadd("success", info);
                        } else {
                            log.info("{}, 抢购失败", info);
                            syncCommands.sadd("fail", info);
                        }
                    } else {
                        log.info("{}, 抢购失败, 没有库存", info);
                        return;
                    }
                } catch (Exception e) {
                    log.error("error: {}", e.getMessage(), e);
                }
            });
        }
    }

}

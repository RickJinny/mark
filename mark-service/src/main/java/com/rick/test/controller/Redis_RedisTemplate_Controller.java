package com.rick.test.controller;

import com.rick.common.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping(value = "/redisTemplate")
public class Redis_RedisTemplate_Controller {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @ResponseBody
    @PostMapping(value = "/test")
    public ServerResponse<String> testRedisTemplate() {
        // String 类型相关操作
        stringType();










        return ServerResponse.createBySuccess("success");
    }

    /**
     * String类型相关操作
     */
    private void stringType() {
        System.out.println("-------------- RedisTemplate String ------------------");
        // 通过 redisTemplate.opsForValue(), 即 ValueOperations 设置
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set("opsForValue_key01", "xiaowang01");
        String key01 = operations.get("opsForValue_key01");
        log.info("opsForValue_key01: {}", key01);
        // 通过 redisTemplate.boundValueOps 设置
        redisTemplate.boundValueOps("boundValueOps_key02").set("xiaowang02");
        String key02 = redisTemplate.boundValueOps("boundValueOps_key02").get();
        log.info("boundValueOps_key02: {}", key02);
        redisTemplate.boundValueOps("boundValueOps_key03").set("xiaowang03", 1, TimeUnit.MINUTES);
        String key03 = redisTemplate.boundValueOps("boundValueOps_key03").get();
        log.info("boundValueOps_key03: {}", key03);

        // 设置过期时间
        redisTemplate.boundValueOps("boundValueOps_key02").expire(10, TimeUnit.MINUTES);
        redisTemplate.expire("boundValueOps_key02", 12, TimeUnit.MINUTES);
        key02 = redisTemplate.boundValueOps("boundValueOps_key02").get();
        log.info("boundValueOps_key02: {}", key02);

        // 删除 key
        redisTemplate.delete("boundValueOps_key02");
        key02 = redisTemplate.boundValueOps("boundValueOps_key02").get();
        log.info("boundValueOps_key02: {}", key02);

        // 顺序递增
        redisTemplate.boundValueOps("boundValueOps_key02").increment(10L);
        key02 = redisTemplate.boundValueOps("boundValueOps_key02").get();
        log.info("boundValueOps_key02: {}", key02);

        // 顺序递减
        redisTemplate.boundValueOps("boundValueOps_key02").increment(-10L);
        key02 = redisTemplate.boundValueOps("boundValueOps_key02").get();
        log.info("boundValueOps_key02: {}", key02);

        System.out.println("-------------- RedisTemplate String ------------------");
    }


    /**
     * 删除 key
     */
    private Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 指定 key 的失效时间
     */
    private Boolean expire(String key, long time) {
        return redisTemplate.expire(key, time, TimeUnit.MINUTES);
    }

    /**
     * 根据 key 获取过期时间
     */
    private Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 判断 key 是否存在
     */
    private Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }
}

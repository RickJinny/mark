package com.rick.test.controller;

import com.alibaba.fastjson.JSON;
import com.rick.common.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;
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
        // Hash 类型相关操作
        hashType();

        return ServerResponse.createBySuccess("success");
    }

    /**
     * Hash 类型相关操作
     */
    private void hashType() {
        System.out.println("-------------- RedisTemplate Hash Type ------------------");
        // 1、添加缓存 (三种方式)
        // 方式一: 通过 redisTemplate.opsForHash() 添加缓存
        redisTemplate.opsForHash().put("user01", "name", "zhangsan01");
        redisTemplate.opsForHash().put("user01", "age", "20");
        Set<Object> user01HashKeySet = redisTemplate.opsForHash().keys("user01");
        List<Object> user01HashValueList = redisTemplate.opsForHash().values("user01");
        Map<Object, Object> user01 = redisTemplate.opsForHash().entries("user01");
        log.info("redisTemplate.opsForHash, user01HashKeySet: {}, user01HashValueList:{}, user01:{}",
                JSON.toJSONString(user01HashKeySet), JSON.toJSONString(user01HashValueList), JSON.toJSONString(user01));
        // 方式二: 通过  HashOperations 添加 hash 类型缓存
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        hashOperations.put("user02", "name", "zhangsan02");
        hashOperations.put("user02", "age", "20");
        Map<Object, Object> user02 = hashOperations.entries("user02");
        String user02Name = (String) hashOperations.get("user02", "name");
        String user02Age = (String) hashOperations.get("user02", "age");
        log.info("RedisTemplate Hash, user02: {}, user02Name: {}, user02Age: {} .", JSON.toJSONString(user02), user02Name, user02Age);
        // 方式三: 通过 redisTemplate.boundHashOps() 添加缓存, 即 BoundHashOperations 添加 Hash 类型
        BoundHashOperations<String, Object, Object> boundHashOperations = redisTemplate.boundHashOps("user03");
        boundHashOperations.put("name", "zhangsan03");
        boundHashOperations.put("age", "23");
        Map<Object, Object> user03 = redisTemplate.boundHashOps("user03").entries();
        Set<Object> user03HashKeySet = redisTemplate.boundHashOps("user03").keys();
        List<Object> user03HashValueList = redisTemplate.boundHashOps("user03").values();
        String user03Name = (String) redisTemplate.boundHashOps("user03").get("name");
        String user03Age = (String) redisTemplate.boundHashOps("user03").get("age");
        log.info("RedisTemplate Hash, user03: {}, user03HashKeySet: {}, user03HashValueList: {}, user03Name: {}, user03Age: {}",
                JSON.toJSONString(user03), JSON.toJSONString(user03HashKeySet), JSON.toJSONString(user03HashValueList), user03Name, user03Age);

        System.out.println("-------------- RedisTemplate Hash Type ------------------");
    }

    /**
     * String类型相关操作
     */
    private void stringType() {
        System.out.println("-------------- RedisTemplate String Type ------------------");
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

        System.out.println("-------------- RedisTemplate String Type ------------------");
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

package com.rick.test.controller;

import com.alibaba.fastjson.JSON;
import com.rick.common.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.*;

@Slf4j
@RestController
@RequestMapping(value = "/redis")
public class RedisController {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @ResponseBody
    @PostMapping(value = "/testRedis")
    public ServerResponse<String> testRedis() {
        // 和 Redis 服务创建连接，参数为 Redis 服务器，所在机器的 ip 和 Redis 的端口号
        Jedis jedis = new Jedis("192.168.0.121", 6379);
        // 第一种类型: String
        stringType(jedis);
        // 第二种类型: Hash
        hashType(jedis);
        // 第三种类型: list
        listType(jedis);

        // 关闭连接
        jedis.close();
        return ServerResponse.createBySuccess("success");
    }

    /**
     * 第三种类型：list
     */
    private void listType(Jedis jedis) {
        System.out.println("------------List------------------");
        jedis.lpush("mykey01", "111", "222", "333", "hello", "world");
        List<String> myPush = jedis.lrange("mykey01", 0, 3);
        log.info("listType mykey01: {}", JSON.toJSONString(myPush));

        System.out.println("------------List------------------");
    }

    /**
     * 第二种类型：Hash
     */
    private void hashType(Jedis jedis) {
        System.out.println("------------Hash------------------");
        jedis.hset("user", "name", "zhangsan");
        jedis.hset("user", "age", "28");
        jedis.hset("user", "name", "lisi");
        jedis.hset("user", "age", "36");

        Map<String, String> map = jedis.hgetAll("user");
        log.info("hashType user: {}", JSON.toJSONString(map));

        String names = jedis.hget("user", "name");
        log.info("hashType user name: {}", names);

        String ages = jedis.hget("user", "age");
        log.info("hashType user age: {}", ages);
        
        System.out.println("------------Hash------------------");
    }

    /**
     * 第一种类型: String
     */
    private void stringType(Jedis jedis) {
        System.out.println("------------String------------------");
        jedis.set("key01", "Hello World!");
        String key01Value = jedis.get("key01");
        log.info("string set key01: {}", key01Value);

        jedis.incr("counter01");
        String counter01Value = jedis.get("counter01");
        log.info("string incr counter01: {}", counter01Value);

        jedis.incrBy("counter01", 200);
        String counter01Value2 = jedis.get("counter01");
        log.info("string incrBy counter01: {}", counter01Value2);

        jedis.decr("counter01");
        String counter01Value3 = jedis.get("counter01");
        log.info("string decr counter01: {}", counter01Value3);

        jedis.decrBy("counter01", 100);
        String counter01Value4 = jedis.get("counter01");
        log.info("string decrBy counter01: {}", counter01Value4);

        System.out.println("------------String------------------");

    }

    @ResponseBody
    @PostMapping(value = "/testRedisPool")
    public ServerResponse<String> testRedisPool() {
        // 创建连接池，连接池为单例，参数为 Redis 服务器所在机器的 ip 和端口号
        JedisPool jedisPool = new JedisPool("192.168.0.121", 6379);
        // 从连接池中获取一个连接
        Jedis jedis = jedisPool.getResource();
        jedis.set("testRedisPool_time", String.valueOf(System.currentTimeMillis()));
        String result = jedis.get("testRedis_time");
        log.info("result: {}", result);
        // 关闭连接
        jedis.close();
        return ServerResponse.createBySuccess(result);
    }

    @ResponseBody
    @PostMapping(value = "/testRedisCluster")
    public ServerResponse<String> testRedisCluster() {
        // 设置集群中的节点
        Set<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("192.168.0.121", 6379));

        // 创建集群操作对象
        JedisCluster jedisCluster = new JedisCluster(nodes);
        jedisCluster.set("testRedisPool_time", String.valueOf(System.currentTimeMillis()));
        String result = jedisCluster.get("testRedisPool_time");
        log.info("result: {}", result);
        jedisCluster.close();
        return ServerResponse.createBySuccess(result);
    }

    @ResponseBody
    @PostMapping(value = "/testRedisTemplate")
    public ServerResponse<String> testRedisTemplate() {
        Map<String, Object> map = new HashMap<>();
        map.put("111", "hello");
        map.put("222", 000);

        redisTemplate.opsForHash().putAll("hash", map);
        Map<Object, Object> hash = redisTemplate.opsForHash().entries("hash");
        return ServerResponse.createBySuccess(JSON.toJSONString(hash));
    }

}

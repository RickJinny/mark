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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
        // 向 Redis 中 set 值
        jedis.set("testRedis_time", String.valueOf(System.currentTimeMillis()));
        String result = jedis.get("testRedis_time");
        log.info("result: {}", result);
        // 关闭连接
        jedis.close();
        return ServerResponse.createBySuccess(result);
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

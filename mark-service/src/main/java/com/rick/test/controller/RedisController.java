package com.rick.test.controller;

import com.rick.common.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

@Slf4j
@RestController
@RequestMapping(value = "/redis")
public class RedisController {

    @ResponseBody
    @PostMapping(value = "/testRedis")
    public ServerResponse<String> testRedis() {
        // 和 Redis 服务创建连接，参数为 Redis 服务器，所在机器的 ip 和 Redis 的端口号
        Jedis jedis = new Jedis("192.168.0.121", 6379);
        // 向 Redis 中 set 值
        jedis.set("testRedis time", String.valueOf(System.currentTimeMillis()));
        String result = jedis.get("testRedis time");
        log.info("result: {}", result);
        return ServerResponse.createBySuccess(result);
    }



}

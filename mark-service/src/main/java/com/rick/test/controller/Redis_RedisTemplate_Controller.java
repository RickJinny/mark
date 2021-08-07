package com.rick.test.controller;

import com.rick.common.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/redisTemplate")
public class Redis_RedisTemplate_Controller {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @ResponseBody
    @PostMapping(value = "/test")
    public ServerResponse<String> testRedisTemplate() {
        redisTemplate.opsForValue().set("redisTemplate01", "hello world");
        String redisTemplate01 = redisTemplate.opsForValue().get("redisTemplate01");
        log.info("testRedisTemplate redisTemplate01: {}", redisTemplate01);
        return ServerResponse.createBySuccess("success");
    }
}

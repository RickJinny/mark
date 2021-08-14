package com.rick.test.controller;

import com.rick.common.ServerResponse;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/lettuce")
public class Redis_Lettuce_Controller {

    @ResponseBody
    @PostMapping(value = "/test")
    public ServerResponse<String> testRedisLettuce() {
        RedisClient redisClient = RedisClient.create(RedisURI.Builder.redis("192.168.0.121", 6379).build());
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        RedisCommands<String, String> syncCommands = connection.sync();
        syncCommands.set("lettuce_key01", "xiaowang");
        String value01 = syncCommands.get("lettuce_key01");
        log.info("lettuce_key01: {}", value01);

        connection.close();
        redisClient.shutdown();
        return ServerResponse.createBySuccess("success");
    }

}

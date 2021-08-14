package com.rick.test.controller;

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

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@RestController
@RequestMapping(value = "/lettuce")
public class Redis_Lettuce_Controller {

    @ResponseBody
    @PostMapping(value = "/test")
    public ServerResponse<String> testRedisLettuce() {
        RedisClient redisClient = RedisClient.create(RedisURI.Builder.redis("192.168.0.121", 6379).build());
        StatefulRedisConnection<String, String> connection = redisClient.connect();

        // 同步操作
        RedisCommands<String, String> syncCommands = connection.sync();
        syncCommands.set("lettuce_key01", "xiaowang");
        String value01 = syncCommands.get("lettuce_key01");
        log.info("lettuce_key01: {}", value01);
        // 同步操作，模拟抢票
        useSyncCommands(syncCommands);


        connection.close();
        redisClient.shutdown();
        return ServerResponse.createBySuccess("success");
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

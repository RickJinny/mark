package com.rick.test.config;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LettuceConfig {

    // Redis 地址
    private final static String HOST = "192.168.0.121";

    // Redis 端口号
    private final static Integer PORT = 6379;


    /**
     * Lettuce 连接配置，Redis 单个实例
     */
    @Bean(name = "redisClient")
    public RedisClient redisClient() {
        RedisURI redisURI = RedisURI.Builder
                .redis(HOST, PORT)
                .build();
        return RedisClient.create(redisURI);
    }
}

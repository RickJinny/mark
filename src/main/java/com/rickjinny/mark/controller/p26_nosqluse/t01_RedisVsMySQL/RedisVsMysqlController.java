package com.rickjinny.mark.controller.p26_nosqluse.t01_RedisVsMySQL;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;


/**
 * 比较一下，从 MySQL 和 Redis 随机读取单条数据的性能。
 */
@RestController
@RequestMapping(value = "/redisVsMysql")
@Slf4j
public class RedisVsMysqlController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RequestMapping(value = "/redis01")
    public void redis01() {
        // 使用随机的 key 来查询 value, 结果应该是 PAYLOAD
        Assert.assertTrue(stringRedisTemplate.opsForValue()
                .get("item" + (ThreadLocalRandom.current().nextInt(RedisVsMysqlAppController.ROWS) + 1))
                .equals(RedisVsMysqlAppController.PAYLOAD));
    }

    @RequestMapping(value = "/redis02")
    public void redis02() {
        Assert.assertTrue(stringRedisTemplate.keys("item71*").size() == 1111);
    }

    @RequestMapping(value = "/mysql01")
    public void mysql01() {
        // 根据随机 name 来查询 data，name 字段有索引，结果应该等于 PAYLOAD
        Assert.assertTrue(jdbcTemplate
                .queryForObject("SELECT data FROM `r` WHERE name = ?", new Object[]{"item" +
                        (ThreadLocalRandom.current().nextInt(RedisVsMysqlAppController.ROWS) + 1)}, String.class)
                .equals(RedisVsMysqlAppController.PAYLOAD));
    }

    @RequestMapping(value = "/mysql02")
    public void mysql02() {
        Assert.assertTrue(jdbcTemplate.queryForList("SELECT name FROM `r` where name like 'item71%'",
                String.class).size() == 1111);
    }
}

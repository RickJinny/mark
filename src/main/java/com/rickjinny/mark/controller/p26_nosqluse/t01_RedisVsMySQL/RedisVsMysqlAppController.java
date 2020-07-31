package com.rickjinny.mark.controller.p26_nosqluse.t01_RedisVsMySQL;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Redis是一款设计简洁的缓存数据库，数据都保存在内存中，所以读写单一 key 的性能非常高。
 * 做一个简单的测试：分别填充 10万条数据到 Redis 和 MySQL 中。MySQL 中的 name 字段做了索引，相当于 Redis 的 key,
 * data 字段为 100 字节的数据，相当于 Redis 的 value。
 */
@Slf4j
@RestController
@RequestMapping(value = "/redisVsMysqlApp")
public class RedisVsMysqlAppController {

    // 模拟 10万条数据存到 Redis 和 MySQL
    private static final int ROWS = 100_000;

    public static final String PAYLOAD = IntStream.rangeClosed(1, 100).mapToObj(__ -> "a").collect(Collectors.joining(""));

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private StandardEnvironment standardEnvironment;

    @PostConstruct
    public void init() {
        // 使用 -Dspring.profiles.active = init 启动程序进行初始化
        if (Arrays.stream(standardEnvironment.getActiveProfiles()).allMatch(s -> s.equalsIgnoreCase("init"))) {
            initRedis();
            initMySQL();
        }
    }

    /**
     * 填充数据到 MySQL
     */
    private void initMySQL() {
        // 删除表
        jdbcTemplate.execute("DROP TABLE IF EXISTS `r`;");

        // 新建表，name 字段做了索引
        jdbcTemplate.execute("CREATE TABLE `r` (\n" +
                "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
                "  `data` varchar(2000) NOT NULL,\n" +
                "  `name` varchar(20) NOT NULL,\n" +
                "  PRIMARY KEY (`id`),\n" +
                "  KEY `name` (`name`) USING BTREE\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;");

        // 批量插入数据
        String sql = "INSERT INTO `r` (`data`, `name`) values (?,?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setString(1, PAYLOAD);
                preparedStatement.setString(2, "item" + i);
            }

            @Override
            public int getBatchSize() {
                return ROWS;
            }
        });
    }

    /**
     * 填充数据到 Redis
     */
    private void initRedis() {
        IntStream.rangeClosed(1, ROWS).forEach(i -> stringRedisTemplate.opsForValue().set("item" + i, PAYLOAD));
        log.info("init redis finished with count: {}", stringRedisTemplate.keys("item*").size());
    }
}

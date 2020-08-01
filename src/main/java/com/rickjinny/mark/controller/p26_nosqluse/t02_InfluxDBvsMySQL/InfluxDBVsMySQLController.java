package com.rickjinny.mark.controller.p26_nosqluse.t02_InfluxDBvsMySQL;


import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.influxdb.BatchOptions;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
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
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * InfluxDB 是一款优秀的时序数据库。这里使用 InfluxDB 来做的 Metrics 打点。
 * 时序数据库的优势是：在于处理指标数据的聚合，并且读写效率非常高。
 */
@Slf4j
@RestController
@RequestMapping(value = "/influxDBVsMySQL")
public class InfluxDBVsMySQLController {

    // 模拟 10万条数据存到 Redis 和 MySQL
    public static final int ROWS = 100_000;

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
            initInfluxDB();
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
     * 初始化 InfluxDB。
     *
     * InfluxDB 批量插入 1000万条数据仅用了 54秒，相当于每秒插入 18万条数据，速度相当快；MySQL的批量插入，速度也挺快达到了每秒 4.8 万。
     */
    private void initInfluxDB() {
        long begin = System.currentTimeMillis();
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder()
                .connectTimeout(1, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);

        try (InfluxDB influxDB = InfluxDBFactory.connect("http://127.0.0.1:8080", "root", "root", builder)) {
            String db = "performance";
            influxDB.query(new Query("DROP DATABASE" + db));
            influxDB.query(new Query("CREATE DATABASE" + db));
            // 设置数据库
            influxDB.setDatabase(db);
            // 批量插入，10000 条数据刷一次，或1秒刷一次
            influxDB.enableBatch(BatchOptions.DEFAULTS.actions(10000).flushDuration(1000));
            IntStream.rangeClosed(1, ROWS).mapToObj(i -> Point
                    .measurement("m")
                    .addField("value", ThreadLocalRandom.current().nextInt(10000))
                    .time(LocalDateTime.now().minusSeconds(5 * i).toInstant(ZoneOffset.UTC).toEpochMilli(), TimeUnit.MILLISECONDS)
                    .build())
                    .forEach(influxDB::write);
            influxDB.flush();
            log.info("init influxDB finished with count {} took {}ms",
                    influxDB.query(new Query("select count(*) from m"))
                            .getResults()
                            .get(0)
                            .getSeries()
                            .get(0)
                            .getValues()
                            .get(0)
                            .get(1), System.currentTimeMillis() - begin);
        }

    }
}

package com.rickjinny.mark.controller.p26_nosqluse.t02_InfluxDBvsMySQL;

import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/performance")
@Slf4j
public class PerformanceController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "mysql")
    public void mysql() {
        long begin = System.currentTimeMillis();
        // 使用 SQL 从 MySQL 查询，按照小时分组
        Object result = jdbcTemplate.queryForList("select date_format(time, '%Y%m%d%H'), max(value), min(value), avg(value) from m " +
                "where time > now() - interval  60 day group by date_format(time, '%Y%m%d%H')");
        log.info("took {} ms result {}", System.currentTimeMillis() - begin, result);
    }

    @RequestMapping(value = "/influxDB")
    public void influxDB() {
        long begin = System.currentTimeMillis();
        try (InfluxDB influxDB = InfluxDBFactory.connect("http://127.0.0.1:8080", "root", "root")) {
            influxDB.setDatabase("performance");
            Object result = influxDB.query(new Query("select mean(value), min(value), max(value) from m " +
                    "where time > now() - 60d group by time(1h)"));
            log.info("took {} ms result {}", System.currentTimeMillis() - begin, result);
        }
    }
}

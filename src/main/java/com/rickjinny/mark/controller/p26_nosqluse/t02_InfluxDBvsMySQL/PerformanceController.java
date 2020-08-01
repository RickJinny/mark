package com.rickjinny.mark.controller.p26_nosqluse.t02_InfluxDBvsMySQL;

import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 对 1000 万数据进行一个统计，查找最近 60 天的数据，按照 1 小时的时间粒度聚合，统计 value 列的最大值、最小值和平均值，并将统计结果绘制成曲线图。
 * 查询结果：调用接口，可以得到 MySQL 查询一次耗时 29s 左右，而 InfluxDB 耗时 980ms。
 *
 * 在按照时间区间聚合的案例上，我们看到了 InfluxDB 的性能优势，我们肯定不能把 InfluxDB 当作普通数据库，原因如下：
 * 第一、InfluxDB 不支持数据更新操作，毕竟时间数据只能随着时间产生新数据，肯定无法对过去的数据做修改。
 * 第二、从数据结构上说，时间序列数据，数据没有单一的主键标识，必须包含时间戳，数据只能和时间戳进行关联，不适合普通业务数据。
 *
 * 总结一下：对于 MySQL 而言，针对大量的数据使用全表扫表的方式来聚合统计指标数据，性能非常差，一般只能作为临时方案来使用。
 * 此时，引入 InfluxDB 之类的时间序列数据库，就很有必要了。时间序列数据库可以作为特定场景（比如监控、统计）的主存储，也可以
 * 和关系型数据库搭配使用，作为一个辅助数据源，保存业务系统的指标数据。
 */
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

package com.rick.test.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shardingsphere.api.config.sharding.KeyGeneratorConfiguration;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.InlineShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class DataSourceConfig {

    /**
     * 创建数据源
     */
    public static Map<String, DataSource> createDataSourceMap() {
        Map<String, DataSource> result = new HashMap<>();
        // 数据源1
        result.put("db01", createDataSourceMap("benz_01"));
        // 数据源2
        result.put("db02", createDataSourceMap("benz_02"));
        // 数据源3
        result.put("db03", createDataSourceMap("user_db"));
        return result;
    }

    private static DataSource createDataSourceMap(String dataSourceName) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://192.168.0.117:3306/" + dataSourceName + "?useUnicode=true");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        return dataSource;
    }

    /**
     * 定义主键生成策略
     */
    public static KeyGeneratorConfiguration getKeyGeneratorConfiguration() {
        KeyGeneratorConfiguration result = new KeyGeneratorConfiguration("SNOWFLAKE", "order_id");
        return result;
    }

    public TableRuleConfiguration getOrderTableRuleConfiguration() {
        // 配置 tb_order 表规则
        TableRuleConfiguration tableRuleConfiguration = new TableRuleConfiguration("tb_order", "db0${1..2}.tb_order_$->{0..7}");
        // 配置数据库表分片策略
        tableRuleConfiguration.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("user_id", "db0${user_id % 2 + 1 }"));
        tableRuleConfiguration.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("order_id", "tb_order_$->{order_id % 8}"));
        tableRuleConfiguration.setKeyGeneratorConfig(getKeyGeneratorConfiguration());
        return tableRuleConfiguration;
    }

    /**
     * 定义 Sharding-jdbc 数据源
     */
    @Bean
    public DataSource getShardingDataSource() throws SQLException {
        // 配置分片规则
        ShardingRuleConfiguration shardingRuleConfiguration = new ShardingRuleConfiguration();
        shardingRuleConfiguration.getTableRuleConfigs().add(getOrderTableRuleConfiguration());
        // 每个库都有公共表 tb_dict，所以这里应该配置一个广播，通知每个库都进行存取数据
        shardingRuleConfiguration.getBroadcastTables().add("tb_dict");
        Properties properties = new Properties();
        properties.put("sql.show", "true");
        // 创建数据源
        return ShardingDataSourceFactory.createDataSource(createDataSourceMap(), shardingRuleConfiguration, properties);
    }
}

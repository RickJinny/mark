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
public class ShardingJdbcConfig {


    /**
     * 定义数据源
     */
    public Map<String, DataSource> createDataSourceMap() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        druidDataSource.setUrl("jdbc:mysql://192.168.0.117:3306/benz?useUnicode=true");
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("123456");
        Map<String, DataSource> result = new HashMap<>();
        result.put("db01", druidDataSource);
        return result;
    }

    /**
     * 定义主键生成策略
     */
    public static KeyGeneratorConfiguration getKeyGeneratorConfiguration() {
        KeyGeneratorConfiguration result = new KeyGeneratorConfiguration("SNOWFLAKE", "order_id");
        return result;
    }

    public TableRuleConfiguration getOrderTableRuleConfiguration() {
        TableRuleConfiguration result = new TableRuleConfiguration("tb_order", "db01.tb_order_$->{1..8}");
        result.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("order_id", "tb_order_$->{order_id % 2 + 1}"));
        result.setKeyGeneratorConfig(getKeyGeneratorConfiguration());
        return result;
    }

    /**
     * 定义 Sharding-jdbc 数据源
     */
    @Bean
    public DataSource getShardingDataSource() throws SQLException {
        ShardingRuleConfiguration shardingRuleConfiguration = new ShardingRuleConfiguration();
        shardingRuleConfiguration.getTableRuleConfigs().add(getOrderTableRuleConfiguration());
        Properties properties = new Properties();
        properties.put("sql.show", "true");
        return ShardingDataSourceFactory.createDataSource(createDataSourceMap(), shardingRuleConfiguration, properties);
    }


}

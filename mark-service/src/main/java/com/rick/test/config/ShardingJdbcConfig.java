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
        // 数据源1
        DruidDataSource dataSource01 = new DruidDataSource();
        dataSource01.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource01.setUrl("jdbc:mysql://192.168.0.117:3306/benz?useUnicode=true");
        dataSource01.setUsername("root");
        dataSource01.setPassword("123456");

        // 数据源2
        DruidDataSource dataSource02 = new DruidDataSource();
        dataSource02.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource02.setUrl("jdbc:mysql://192.168.0.117:3306/benz?useUnicode=true");
        dataSource02.setUsername("root");
        dataSource02.setPassword("123456");

        Map<String, DataSource> result = new HashMap<>();
        result.put("db01", dataSource01);
        result.put("db02", dataSource02);

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
        TableRuleConfiguration result = new TableRuleConfiguration("tb_order", "db01.tb_order_$->{0..7}");
        result.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("order_id", "tb_order_$->{order_id % 8}"));
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

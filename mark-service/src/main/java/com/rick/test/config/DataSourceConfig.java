package com.rick.test.config;

import org.springframework.context.annotation.Configuration;

/**
 * 数据源配置
 */
@Configuration
public class DataSourceConfig {

//    @Value("${spring.datasource.driver-class-name}")
//    private String driver ;
//
//    @Value("${spring.datasource.url}")
//    private String url;
//
//    @Value("${spring.datasource.username}")
//    private String username;
//
//    @Value("${spring.datasource.password}")
//    private String password;
//
//    @Bean
//    public HikariDataSource dataSource() {
//        HikariConfig config = new HikariConfig();
//        config.setDriverClassName(driver);
//        config.setJdbcUrl(url);
//        config.setUsername(username);
//        config.setPassword(password);
//        HikariDataSource dataSource = new HikariDataSource(config);
//        return dataSource;
//    }
}

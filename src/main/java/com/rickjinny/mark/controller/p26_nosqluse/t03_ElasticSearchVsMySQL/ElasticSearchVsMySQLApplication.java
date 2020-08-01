package com.rickjinny.mark.controller.p26_nosqluse.t03_ElasticSearchVsMySQL;

import com.rickjinny.mark.utils.PropertyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@Slf4j
// 明确设置哪个是 ES 的 Repository
@EnableElasticsearchRepositories(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = NewsESRepository.class))
// 其它是 MySQL 的 Repository
@EnableJpaRepositories(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = NewsESRepository.class))
public class ElasticSearchVsMySQLApplication {

    @Autowired
    private StandardEnvironment standardEnvironment;

    @Autowired
    private NewsESRepository newsESRepository;

    @Autowired
    private NewsMySQLRepository newsMySQLRepository;

    public static void main(String[] args) {
        PropertyUtils.loadPropertySource(ElasticSearchVsMySQLApplication.class, "es.properties");
        SpringApplication.run(ElasticSearchVsMySQLApplication.class, args);
    }

    
}

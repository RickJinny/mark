package com.rickjinny.mark.controller.p26_nosqluse.t03_ElasticSearchVsMySQL;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
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

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

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

    @PostConstruct
    public void init() {
        // 使用 -Dspring.profiles.active = init 启动程序进行初始化
        if (Arrays.stream(standardEnvironment.getActiveProfiles()).anyMatch(s -> s.equalsIgnoreCase("init"))) {
            // csv 中的原始数据，只有 4000 条
            List<News> newsList = loadData();
            AtomicLong atomicLong = new AtomicLong();
            newsList.forEach(item -> item.setTitle("%%" + item.getTitle()));
            // 我们模拟 100 倍的数据量，也就是 40 万条
            IntStream.rangeClosed(1, 100).forEach(repeat -> {
                newsList.forEach(item -> {
                    // 重新设置主键 id
                    item.setId(atomicLong.incrementAndGet());
                    // 每次复制数据，稍微改一下 title 字段，在前面加上一个数字，代表这是第几次复制
                    item.setCategory(item.getTitle().replaceFirst("%%", String.valueOf(repeat)));
                });
                initMySQL(newsList, repeat == 1);
                log.info("init MySQL finished for {}", repeat);
                initES(newsList, repeat == 1);
                log.info("init ES finished for {}", repeat);
            });
        }
    }

    /**
     * 把数据保存到 ES 中
     */
    private void initES(List<News> newsList, boolean clear) {
        if (clear) {
            // 首次调用的时候，先删除历史数据
            newsESRepository.deleteAll();
        }
        newsESRepository.saveAll(newsList);
    }

    /**
     * 把数据保存到 MySQL 中
     */
    private void initMySQL(List<News> newsList, boolean clear) {
        if (clear) {
            // 首次调用的时候，先删除历史数据
            newsMySQLRepository.deleteAll();
        }
        newsMySQLRepository.saveAll(newsList);
    }

    /**
     * 从 news.csv 中解析得到原始数据
     */
    private List<News> loadData() {
        // 使用 jackson - dataformat - csv 实现 csv 到 pojo 的转换
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema schema = CsvSchema.emptySchema().withHeader();
        ObjectReader objectReader = csvMapper.readerFor(News.class).with(schema);
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("news.csv").getFile());
        try (Reader reader = new FileReader(file)) {
            return objectReader.<News>readValues(reader).readAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

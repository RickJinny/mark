package com.rickjinny.mark.controller.p26_nosqluse.t03_ElasticSearchVsMySQL;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.IntStream;

/**
 * 分别调用mysql接口和es接口进行测试，结果为：
 * ES 耗时仅仅为 48ms, MySQL耗时 6s，MySQL 耗时是 ES 的 100 倍。
 * 虽然新闻分类id，已经建立索引，但是这个索引只能起到加速过滤分类 id 这一单一条件的作用，对于文本内容的全文搜索，B+树索引无能为力。
 */
@RestController
@RequestMapping(value = "/performance")
@Slf4j
public class PerformanceController {

    @Autowired
    private NewsMySQLRepository newsMySQLRepository;

    @Autowired
    private NewsESRepository newsESRepository;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 测试 MySQL 搜索，最后输出耗时和结果
     */
    @RequestMapping(value = "/mysql")
    public void mysql(@RequestParam(value = "cateId", defaultValue = "1") int cateId,
                      @RequestParam(value = "keyword1", defaultValue = "aa") String keyword1,
                      @RequestParam(value = "keyword2", defaultValue = "bb") String keyword2) {
        long begin = System.currentTimeMillis();
        Object result = newsMySQLRepository.countByCateIdAndContentContainingAndContentContaining(cateId, keyword1, keyword2);
        log.info("took {} ms result {}", System.currentTimeMillis() - begin, result);
    }

    /**
     * 测试 ES 搜索，最后输出耗时和结果
     */
    @RequestMapping(value = "/es")
    public void es(@RequestParam(value = "cateId", defaultValue = "1") Integer cateId,
                   @RequestParam(value = "keyword1", defaultValue = "aa") String keyword1,
                   @RequestParam(value = "keyword2", defaultValue = "bb") String keyword2) {
        long begin = System.currentTimeMillis();
        Object result = newsESRepository.countByCateIdAndContentContainingAndContentContaining(cateId, keyword1, keyword2);
        log.info("took {} ms result {}", System.currentTimeMillis() - begin, result);
    }

    /**
     * ES 这种以索引为核心的数据库，也不是万能的，频繁更新就是一大问题。
     * 验证一下 JdbcTemplate + SQL 语句
     */
    public void mysql2(@RequestParam(value = "id", defaultValue = "400_000") Long id) {
        long begin = System.currentTimeMillis();
        // 对于 MySQL，使用 JdbcTemplate + SQL 语句，实现直接更新某个 category 字段，更新 1000 次
        IntStream.rangeClosed(1, 1000).forEach(i -> {
            jdbcTemplate.update("update `new` set category = ? where  id = ?", new Object[]{"test" + i, id});
        });
        log.info("mysql took {} ms result {}", System.currentTimeMillis() - begin, newsMySQLRepository.findById(id));
    }

    /**
     * 验证一下 ElasticsearchTemplate + 自定义 UpdateQuery
     */
    @RequestMapping(value = "/es2")
    public void es2(@RequestParam(value = "id", defaultValue = "400_000") Long id) {
        long start = System.currentTimeMillis();
        IntStream.rangeClosed(1, 1000).forEach(i -> {
            // 对于 ES, 通过 ElasticsearchTemplate + 自定义UpdateQuery，实现UpdateQuery实现文档的部分更新
            UpdateQuery updateQuery;
            try {
                //

            } catch (Exception e) {
                e.printStackTrace();
            }
//            elasticsearchRestTemplate.update(updateQuery);
        });
    }
}

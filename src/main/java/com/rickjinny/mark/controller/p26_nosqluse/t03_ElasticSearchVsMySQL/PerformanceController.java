package com.rickjinny.mark.controller.p26_nosqluse.t03_ElasticSearchVsMySQL;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}

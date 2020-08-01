package com.rickjinny.mark.controller.p26_nosqluse.t03_ElasticSearchVsMySQL;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * 使用 Spring Data，直接定义 Repository，然后直接定义查询方法，无需实现任何逻辑即可实现查询。
 * Spring Data 会根据方法名生产相应的 SQL 语句 和 ES 查询 DSL。
 */
@Repository
public interface NewsESRepository extends ElasticsearchRepository<News, Long> {

    /**
     * ES: 搜索分类等于 cateId 参数，且内容同时包含关键字 keyword1 和 keyword2，计算符合条件的新闻总数量
     */
    Long countByCateIdAndContentContainingAndContentContaining(Integer cateId, String keyword1, String keyword2);

}

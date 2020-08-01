package com.rickjinny.mark.controller.p26_nosqluse.t03_ElasticSearchVsMySQL;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsESRepository extends ElasticsearchRepository<News, Long> {

    Long countByCateIdAndContentContainingAndContentContaining(Integer cateId, String keyword1, String keyword2);

}

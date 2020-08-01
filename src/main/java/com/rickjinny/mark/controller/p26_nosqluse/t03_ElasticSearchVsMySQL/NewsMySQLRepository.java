package com.rickjinny.mark.controller.p26_nosqluse.t03_ElasticSearchVsMySQL;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsMySQLRepository extends JpaRepository<News, Long> {

    Long countByCateIdAndContentContainingAndContentContaining(Integer cateId, String keyword1, String keyword2);

}

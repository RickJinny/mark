package com.rickjinny.mark.controller.p26_nosqluse.t03_ElasticSearchVsMySQL;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


/**
 * 定义一个实体 News，包含新闻分类、标题、内容等字段。这个实体同时会用作 Spring Data JPA 和 Spring Data ElasticSearch 的实体。
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
//@Table(name = "aaa", indexes = {@Index(columnList = "cateid")}) // @Table 注解定义了这是一个 MySQL 表，表名 news，对 cateId 列做索引
@Document(indexName = "news", replicas = 0) // @Document 注解定义了这是一个 ES 索引，索引名称 news，数据不需要冗余
public class News {

    @Id
    private Long id;

    /**
     * 新闻分类名称
     */
    @Field(type = FieldType.Keyword)
    private String category;

    /**
     * 新闻分类id
     */
    private int cateId;

    /**
     * 新闻标题
     * @Column 注解，定义了在 MySQL 字段中，比如这里定义 title 列的类型是 varchar(500)
     * @Field 注解定义了 ES 字段的格式，使用 ik 分词器进行分词
     */
    @Column(columnDefinition = "varchar(500)")
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String title;

    /**
     * 新闻内容
     */
    @Column(columnDefinition = "text")
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String content;
}

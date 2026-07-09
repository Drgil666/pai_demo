package com.example.pai_demo.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author GilbertYoung
 * @date 2026/07/06 16:43
 */

/**
 * 文章详情类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "article_detail")
public class ArticleDetail extends BaseEntity {
    /**
     * 对应的文章id
     */
    @ApiModelProperty(value = "对应的文章id")
    @Column(name = "article_id", nullable = false)
    private Integer articleId;
    /**
     * 文章的版本号（可迭代）
     */
    @ApiModelProperty(value = "文章的版本号（可迭代）")
    @Column(name = "version", nullable = false)
    private Integer version;
    /**
     * 文章内容
     */
    @ApiModelProperty(value = "文章内容")
    @Column(name = "content", columnDefinition = "longtext comment '文章内容'")
    private String content;
}

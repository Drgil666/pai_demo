package com.example.pai_demo.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author GilbertYoung
 * @date 2026/07/06 17:03
 */

/**
 * 文章-标签中间表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "article_tag")
public class ArticleTag extends BaseEntity {
    /**
     * 文章id
     */
    @ApiModelProperty(value = "文章id")
    @Column(name = "article_id", nullable = false)
    private Integer articleId;
    /**
     * 标签id
     */
    @ApiModelProperty(value = "标签id")
    @Column(name = "tag_id", nullable = false)
    private Integer tagId;
}

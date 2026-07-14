package com.example.pai_demo.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author GilbertYoung
 * @date 2026/07/06 16:31
 */

/**
 * 文章相关信息类（不包含正文）
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "article")
public class Article extends BaseEntity {
    /**
     * 文章名称
     */
    @ApiModelProperty(value = "文章名称")
    @Column(name = "title", nullable = false)
    private String title;
    /**
     * 文章发布的用户id
     */
    @ApiModelProperty(value = "文章发布的用户id")
    @Column(name = "user_id")
    private Integer userId;
    /**
     * 文章对应的合集id
     */
    @ApiModelProperty(value = "文章对应的合集id")
    @Column(name = "category_id")
    private Integer categoryId;
    /**
     * 文章审核状态(0表示待审核,1-表示审核通过)
     */
    @ApiModelProperty(value = "文章审核状态")
    @Column(name = "status")
    private Integer status;
}

package com.example.pai_demo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author GilbertYoung
 * @date 2026/07/07 09:54
 */

/**
 * 评论类
 * 顶级评论
 * |----回复
 * |----回复的回复
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "comment")
@ApiModel(value = "评论", description = "评论类，支持顶级评论及多级回复")
public class Comment extends BaseEntity {
    /**
     * 评论对应的文章id
     */
    @ApiModelProperty(value = "评论对应的文章id")
    @Column(name = "article_id", nullable = false)
    private Integer articleId;
    /**
     * 评论对应的用户id
     */
    @ApiModelProperty(value = "评论对应的用户id")
    @Column(name = "user_id", nullable = false)
    private Integer userId;
    /**
     * 评论内容
     */
    @ApiModelProperty(value = "评论内容")
    @Column(name = "content", nullable = false)
    private String content;
    /**
     * 评论对应的顶级id
     */
    @ApiModelProperty(value = "评论对应的顶级id")
    @Column(name = "top_comment_id", nullable = false)
    private Integer topCommentId;
    /**
     * 评论对应的上级id
     */
    @ApiModelProperty(value = "评论对应的上级id")
    @Column(name = "parent_comment_id", nullable = false)
    private Integer parentCommentId;
}

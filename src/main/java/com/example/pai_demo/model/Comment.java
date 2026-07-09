package com.example.pai_demo.model;

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
public class Comment extends BaseEntity {
    /**
     * 评论对应的文章id
     */
    @Column(name = "article_id", nullable = false)
    private Integer articleId;
    /**
     * 评论对应的用户id
     */
    @Column(name = "user_id", nullable = false)
    private Integer userId;
    /**
     * 评论内容
     */
    @Column(name = "content", nullable = false)
    private String content;
    /**
     * 评论对应的顶级id
     */
    @Column(name = "top_comment_id", nullable = false)
    private Integer topCommentId;
    /**
     * 评论对应的上级id
     */
    @Column(name = "parent_comment_id", nullable = false)
    private Integer parentCommentId;
}

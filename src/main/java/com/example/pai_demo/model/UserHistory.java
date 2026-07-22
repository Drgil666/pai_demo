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
 * @date 2026/07/06 17:09
 */

/**
 * 用户操作流水类
 * 分为阅读、点赞/取消点赞、收藏/取消收藏、发表/删除评论、关注/取消关注
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "user_history")
@ApiModel(value = "用户操作流水类分为阅读、点赞/取消点赞、收藏/取消收藏、发表/删除评论、关注/取消关注", description = "用户操作流水类分为阅读、点赞/取消点赞、收藏/取消收藏、发表/删除评论、关注/取消关注")
public class UserHistory extends BaseEntity {
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    @Column(name = "user_id", nullable = false)
    private Integer userId;
    /**
     * 操作对应的对象id
     */
    @ApiModelProperty(value = "操作对应的对象id")
    @Column(name = "object_id", nullable = false)
    private Integer objectId;
    /**
     * 是否是阅读文章(0-不是,1-是)
     */
    @ApiModelProperty(value = "是否是阅读文章(0-不是,1-是)")
    @Column(name = "is_read", nullable = false)
    private Integer isRead;
    /**
     * 是否是点赞文章(0-不是,1-点赞,2-取消点赞)
     */
    @ApiModelProperty(value = "是否是点赞文章(0-不是,1-点赞,2-取消点赞)")
    @Column(name = "is_like", nullable = false)
    private Integer isLike;
    /**
     * 是否是收藏文章(0-不是,1-收藏,2-取消收藏)
     */
    @ApiModelProperty(value = "是否是收藏文章(0-不是,1-收藏,2-取消收藏)")
    @Column(name = "is_favorite", nullable = false)
    private Integer isFavorite;
    /**
     * 是否是评论文章(0-不是,1-评论,2-删除评论)
     */
    @ApiModelProperty(value = "是否是评论文章(0-不是,1-评论,2-删除评论)")
    @Column(name = "is_comment", nullable = false)
    private Integer isComment;
    /**
     * 是否是关注用户(0-不是,1-关注,2-取消关注)
     */
    @ApiModelProperty(value = "是否是关注用户(0-不是,1-关注,2-取消关注)")
    @Column(name = "is_subscribe", nullable = false)
    private Integer isSubscribe;

    public UserHistory() {
        this.isComment = 0;
        this.isFavorite = 0;
        this.isLike = 0;
        this.isRead = 0;
        this.isSubscribe = 0;
    }
}

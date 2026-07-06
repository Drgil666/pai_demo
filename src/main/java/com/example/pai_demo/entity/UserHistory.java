package com.example.pai_demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author GilbertYoung
 * @date 2026/07/06 17:09
 */

/**
 * 用户操作流水类
 * 分为阅读文章、点赞文章、收藏文章、发表评论、关注用户五种情况
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "user_history")
public class UserHistory extends BaseEntity {
    /**
     * 用户id
     */
    @Column(name = "user_id", nullable = false)
    private Integer userId;
    /**
     * 操作对应的文章id/评论id/用户id
     */
    @Column(name = "object_id", nullable = false)
    private Integer objectId;

    @AllArgsConstructor
    @Getter
    public enum UserHistoryEnum {
        /**
         * 阅读文章
         */
        READ(0, "read"),
        /**
         * 点赞文章
         */
        LIKE(1, "like"),
        /**
         * 收藏文章
         */
        SUBSCRIBE(2, "subscribe"),
        /**
         * 发表评论
         */
        COMMENT(3, "comment"),
        /**
         * 关注用户
         */
        FOLLOW(4, "follow");
        private final Integer code;
        private final String name;
    }

    /**
     * 0-阅读,1-点赞,2-收藏,3-评论
     */
    @Column(name = "type", nullable = false)
    private UserHistoryEnum type;
}

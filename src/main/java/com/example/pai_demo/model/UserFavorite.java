package com.example.pai_demo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author GilbertYoung
 * @date 2026/07/06 17:12
 */

/**
 * 用户收藏类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "user_favorite")
public class UserFavorite extends BaseEntity {
    /**
     * 用户id
     */
    @Column(name = "user_id", nullable = false)
    private Integer userId;
    /**
     * 收藏的文章id
     */
    @Column(name = "article_id", nullable = false)
    private Integer articleId;
}

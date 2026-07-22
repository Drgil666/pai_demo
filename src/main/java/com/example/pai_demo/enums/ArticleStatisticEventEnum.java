package com.example.pai_demo.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author GilbertYoung
 * @date 2026/07/22 15:04
 */
@Getter
public enum ArticleStatisticEventEnum {
    /**
     * 阅读文章
     */
    ARTICLE_READ(1, "article_read"),
    /**
     * 点赞文章
     */
    ARTICLE_LIKE(2, "article_like"),
    /**
     * 评论文章
     */
    ARTICLE_COMMENT(3, "article_comment"),
    /**
     * 收藏文章
     */
    ARTICLE_FAVORITE(4, "article_favorite"),
    /**
     * 取消点赞
     */
    ARTICLE_LIKE_CANCEL(5, "article_like_cancel"),
    /**
     * 删除评论
     */
    ARTICLE_COMMENT_CANCEL(6, "article_comment_cancel"),
    /**
     * 取消收藏
     */
    ARTICLE_FAVORITE_CANCEL(7, "article_favorite_cancel"),
    ;
    private static Map<Integer, ArticleStatisticEventEnum> mapper;

    static {
        mapper = new HashMap<>();
        for (ArticleStatisticEventEnum type : values()) {
            mapper.put(type.type, type);
        }
    }

    private int type;
    private String msg;

    ArticleStatisticEventEnum(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }
}

package com.example.pai_demo.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author GilbertYoung
 * @date 2026/07/22 15:04
 */
@Getter
public enum UserStatisticEventEnum {
    /**
     * 用户发表文章
     */
    USER_ARTICLE(1, "user_article"),
    /**
     * 用户关注用户
     */
    USER_FOLLOW(2, "user_follow"),
    /**
     * 用户新增粉丝
     */
    USER_FOLLOWER(3, "user_follower"),
    /**
     * 用户收藏文章
     */
    USER_FAVORITE(4, "user_favorite"),
    /**
     * 用户删除发表文章
     */
    USER_ARTICLE_CANCEL(5, "user_article_cancel"),
    /**
     * 用户取消关注用户
     */
    USER_FOLLOW_CANCEL(6, "user_follow_cancel"),
    /**
     * 用户减少粉丝
     */
    USER_FOLLOWER_CANCEL(7, "user_follower_cancel"),
    /**
     * 用户取消收藏文章
     */
    USER_FAVORITE_CANCEL(8, "user_favorite_cancel"),
    ;
    private static Map<Integer, UserStatisticEventEnum> mapper;

    static {
        mapper = new HashMap<>();
        for (UserStatisticEventEnum type : values()) {
            mapper.put(type.type, type);
        }
    }

    private int type;
    private String msg;

    UserStatisticEventEnum(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public static UserStatisticEventEnum typeOf(int type) {
        return mapper.get(type);
    }

    public static UserStatisticEventEnum typeOf(String type) {
        return valueOf(type.toUpperCase().trim());
    }

}

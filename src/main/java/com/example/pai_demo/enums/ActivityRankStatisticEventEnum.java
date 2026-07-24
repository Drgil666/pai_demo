package com.example.pai_demo.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author GilbertYoung
 * @date 2026/07/22 15:04
 */
@Getter
public enum ActivityRankStatisticEventEnum {
    /**
     * 用户登录
     */
    USER_LOGIN(1, "user_login"),
    /**
     * 用户点赞
     */
    USER_LIKE(2, "user_like"),
    /**
     * 用户评论
     */
    USER_COMMENT(3, "user_comment"),
    /**
     * 用户发表文章
     */
    USER_PUBLISH(4, "user_publish"),
    ;
    private static Map<Integer, ActivityRankStatisticEventEnum> mapper;

    static {
        mapper = new HashMap<>();
        for (ActivityRankStatisticEventEnum type : values()) {
            mapper.put(type.type, type);
        }
    }

    private int type;
    private String msg;

    ActivityRankStatisticEventEnum(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }
}

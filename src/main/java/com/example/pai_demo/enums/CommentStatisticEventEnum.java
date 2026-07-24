package com.example.pai_demo.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author GilbertYoung
 * @date 2026/07/22 15:04
 */
@Getter
public enum CommentStatisticEventEnum {
    /**
     * 点赞文章
     */
    COMMENT_LIKE(1, "comment_like"),
    /**
     * 取消点赞
     */
    COMMENT_LIKE_CANCEL(2, "comment_like_cancel"),
    ;
    private static Map<Integer, CommentStatisticEventEnum> mapper;

    static {
        mapper = new HashMap<>();
        for (CommentStatisticEventEnum type : values()) {
            mapper.put(type.type, type);
        }
    }

    private int type;
    private String msg;

    CommentStatisticEventEnum(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }
}

package com.example.pai_demo.model.event;

import com.example.pai_demo.enums.CommentStatisticEventEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author GilbertYoung
 * @date 2026/07/22 14:59
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CommentStatisticEvent {
    public static final String COMMENT_STATISTIC_EVENT_PREFIX = "comment_statistic_";
    private Integer commentId;
    private Integer userId;
    private CommentStatisticEventEnum type;
}

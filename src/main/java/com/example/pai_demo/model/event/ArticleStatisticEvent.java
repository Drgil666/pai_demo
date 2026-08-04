package com.example.pai_demo.model.event;

import com.example.pai_demo.enums.ArticleStatisticEventEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author GilbertYoung
 * @date 2026/07/22 14:59
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ArticleStatisticEvent {
    public static final String ARTICLE_STATISTIC_EVENT_PREFIX = "article_statistic_";
    private Integer articleId;
    private Integer userId;
    private ArticleStatisticEventEnum type;
}

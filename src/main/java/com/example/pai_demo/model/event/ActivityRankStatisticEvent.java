package com.example.pai_demo.model.event;

import com.example.pai_demo.enums.ActivityRankStatisticEventEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author GilbertYoung
 * @date 2026/07/24 09:09
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityRankStatisticEvent {
    public static final String ACTIVITY_RANK_STATISTIC_EVENT_PREFIX = "activity_rank_statistic";
    private Integer userId;
    private ActivityRankStatisticEventEnum type;

}

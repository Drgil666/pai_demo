package com.example.pai_demo.model.event;

import com.example.pai_demo.enums.UserStatisticEventEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author GilbertYoung
 * @date 2026/07/22 14:59
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserStatisticEvent {
    public static final String USER_STATISTIC_EVENT_PREFIX = "user_statistic_";
    private Integer userId;
    private UserStatisticEventEnum type;
}

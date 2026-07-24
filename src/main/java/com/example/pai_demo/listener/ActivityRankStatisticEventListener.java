package com.example.pai_demo.listener;

import com.example.pai_demo.dao.TokenDao;
import com.example.pai_demo.model.event.ActivityRankStatisticEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author GilbertYoung
 * @date 2026/07/22 15:56
 */

/**
 * 用户活跃度监听类，监听活跃度
 */
@Component
public class ActivityRankStatisticEventListener {
    private static final String DAILY_KEY = "daily";
    private static final String MONTHLY_KEY = "monthly";
    private static final DateTimeFormatter DAILY_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter MONTHLY_FORMAT = DateTimeFormatter.ofPattern("yyyyMM");
    private static final Integer USER_LOGIN_SCORE = 1;
    private static final Integer USER_LIKE_SCORE = 2;
    private static final Integer USER_COMMENT_SCORE = 3;
    private static final Integer USER_PUBLISH_SCORE = 10;
    @Resource
    private TokenDao tokenDao;

    @EventListener(classes = ActivityRankStatisticEvent.class)
    @Async
    public void activityRankStatisticEventListener(ActivityRankStatisticEvent event) {
        LocalDate now = LocalDate.now();
        String dailyKey = ActivityRankStatisticEvent.ACTIVITY_RANK_STATISTIC_EVENT_PREFIX + ":" + DAILY_KEY + ":" + DAILY_FORMAT.format(now);
        String monthlyKey = ActivityRankStatisticEvent.ACTIVITY_RANK_STATISTIC_EVENT_PREFIX + ":" + MONTHLY_KEY + ":" + MONTHLY_FORMAT.format(now);
        switch (event.getType()) {
            case USER_LOGIN:
                tokenDao.zIncr(dailyKey, event.getUserId().toString(), USER_LOGIN_SCORE);
                tokenDao.zIncr(monthlyKey, event.getUserId().toString(), USER_LOGIN_SCORE);
                break;
            case USER_LIKE:
                tokenDao.zIncr(dailyKey, event.getUserId().toString(), USER_LIKE_SCORE);
                tokenDao.zIncr(monthlyKey, event.getUserId().toString(), USER_LIKE_SCORE);
                break;
            case USER_COMMENT:
                tokenDao.zIncr(dailyKey, event.getUserId().toString(), USER_COMMENT_SCORE);
                tokenDao.zIncr(monthlyKey, event.getUserId().toString(), USER_COMMENT_SCORE);
                break;
            case USER_PUBLISH:
                tokenDao.zIncr(dailyKey, event.getUserId().toString(), USER_PUBLISH_SCORE);
                tokenDao.zIncr(monthlyKey, event.getUserId().toString(), USER_PUBLISH_SCORE);
                break;
            default:
        }
    }
}

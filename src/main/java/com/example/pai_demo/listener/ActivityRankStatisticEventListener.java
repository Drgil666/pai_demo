package com.example.pai_demo.listener;

import com.example.pai_demo.dao.TokenDao;
import com.example.pai_demo.model.event.ActivityRankStatisticEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;

/**
 * @author GilbertYoung
 * @date 2026/07/22 15:56
 */

/**
 * 用户活跃度监听类，监听活跃度
 */
@Component
public class ActivityRankStatisticEventListener {
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
        String dailyKey = tokenDao.getDailyKey();
        String monthlyKey = tokenDao.getMonthlyKey();
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

package com.example.pai_demo.listener;

import com.alibaba.fastjson.JSON;
import com.example.pai_demo.dao.TokenDao;
import com.example.pai_demo.model.event.ActivityRankStatisticEvent;
import com.example.pai_demo.model.message.StatEventMessage;
import com.example.pai_demo.utils.SnowflakeIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.example.pai_demo.rocketmq.RocketMQTopicConfig.TOPIC_STAT_ACTIVITY;

@Component
@Slf4j
public class ActivityRankStatisticEventListener {
    private static final int RETRY_TIMES = 3;
    private static final long RETRY_DELAY_MS = 2000;

    @Resource
    private RocketMQTemplate rocketMQTemplate;
    @Resource
    private TokenDao tokenDao;

    @EventListener(classes = ActivityRankStatisticEvent.class)
    @Async
    public void activityRankStatisticEventListener(ActivityRankStatisticEvent event) {
        StatEventMessage msg = new StatEventMessage(SnowflakeIdUtil.generateId(),
                event.getType().name(), event.getUserId());
        for (int i = 0; i < RETRY_TIMES; i++) {
            try {
                rocketMQTemplate.convertAndSend(TOPIC_STAT_ACTIVITY, msg);
                log.info("Sent: {}", JSON.toJSONString(msg));
                return;
            } catch (Exception e) {
                if (i < RETRY_TIMES - 1) {
                    try {
                        Thread.sleep(RETRY_DELAY_MS);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        }
        log.warn("MQ send failed, fallback to Redis");
        String dailyKey = tokenDao.getDailyKey();
        String monthlyKey = tokenDao.getMonthlyKey();
        String userId = event.getUserId().toString();
        int score = getScore(event.getType().name());
        tokenDao.zIncr(dailyKey, userId, score);
        tokenDao.zIncr(monthlyKey, userId, score);
    }

    private int getScore(String type) {
        switch (type) {
            case "USER_LOGIN":
                return 1;
            case "USER_LIKE":
                return 2;
            case "USER_COMMENT":
                return 3;
            case "USER_PUBLISH":
                return 10;
            default:
                return 0;
        }
    }
}

package com.example.pai_demo.rocketmq.consumer;

import com.alibaba.fastjson.JSON;
import com.example.pai_demo.dao.TokenDao;
import com.example.pai_demo.mapper.StatEventMessageMapper;
import com.example.pai_demo.model.message.StatEventMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.example.pai_demo.rocketmq.RocketMQTopicConfig.GROUP_STAT_ACTIVITY;
import static com.example.pai_demo.rocketmq.RocketMQTopicConfig.TOPIC_STAT_ACTIVITY;

/**
 * 用户活跃度排行消费者（取代 ActivityRankStatisticEventListener）
 * 消费 paistat-activity 消息，异步更新 Redis 日/月活跃度排行
 *
 * @author GilbertYoung
 * @date 2026/07/25 10:00
 */
@Component
@Slf4j
@ConditionalOnProperty(name = "rocketmq.consumer.enabled", havingValue = "true")
@RocketMQMessageListener(topic = TOPIC_STAT_ACTIVITY, consumerGroup = GROUP_STAT_ACTIVITY)
public class ActivityRankStatisticConsumer implements RocketMQListener<String> {
    private static final Long USER_LOGIN_SCORE = 1L;
    private static final Long USER_LIKE_SCORE = 2L;
    private static final Long USER_COMMENT_SCORE = 3L;
    private static final Long USER_PUBLISH_SCORE = 10L;

    @Resource
    private TokenDao tokenDao;
    @Resource(name = "statConsumerExecutor")
    private ThreadPoolTaskExecutor executor;
    @Resource
    private StatEventMessageMapper statEventMessageMapper;

    @Override
    public void onMessage(String message) {
        executor.execute(() -> {
            StatEventMessage msg = JSON.parseObject(message, StatEventMessage.class);
            log.info("Received: {}", message);
            try {
                statEventMessageMapper.create(msg);
            } catch (DuplicateKeyException e) {
                log.info("Duplicate message skipped: msgId={}", msg.getMsgId());
                return;
            }
            try {
                String dailyKey = tokenDao.getDailyKey();
                String monthlyKey = tokenDao.getMonthlyKey();
                String userId = msg.getTargetId().toString();
                switch (msg.getEventType()) {
                    case "USER_LOGIN":
                        tokenDao.zIncr(dailyKey, userId, USER_LOGIN_SCORE);
                        tokenDao.zIncr(monthlyKey, userId, USER_LOGIN_SCORE);
                        break;
                    case "USER_LIKE":
                        tokenDao.zIncr(dailyKey, userId, USER_LIKE_SCORE);
                        tokenDao.zIncr(monthlyKey, userId, USER_LIKE_SCORE);
                        break;
                    case "USER_COMMENT":
                        tokenDao.zIncr(dailyKey, userId, USER_COMMENT_SCORE);
                        tokenDao.zIncr(monthlyKey, userId, USER_COMMENT_SCORE);
                        break;
                    case "USER_PUBLISH":
                        tokenDao.zIncr(dailyKey, userId, USER_PUBLISH_SCORE);
                        tokenDao.zIncr(monthlyKey, userId, USER_PUBLISH_SCORE);
                        break;
                }
            } catch (Exception e) {
                log.error("ActivityRankStatistic consume error, msgId={}", msg.getMsgId(), e);
            }
        });
    }
}

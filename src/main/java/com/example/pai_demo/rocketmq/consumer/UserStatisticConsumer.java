package com.example.pai_demo.rocketmq.consumer;

import com.alibaba.fastjson.JSON;
import com.example.pai_demo.dao.TokenDao;
import com.example.pai_demo.enums.UserStatisticEventEnum;
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

import static com.example.pai_demo.enums.UserStatisticEventEnum.*;
import static com.example.pai_demo.rocketmq.RocketMQTopicConfig.GROUP_STAT_USER;
import static com.example.pai_demo.rocketmq.RocketMQTopicConfig.TOPIC_STAT_USER;

/**
 * 用户统计消费者（取代 UserStatisticEventListener）
 * 消费 paistat-user 消息，异步更新 Redis 用户维度统计
 *
 * @author GilbertYoung
 * @date 2026/07/25 10:00
 */
@Component
@Slf4j
@ConditionalOnProperty(name = "rocketmq.consumer.enabled", havingValue = "true")
@RocketMQMessageListener(topic = TOPIC_STAT_USER, consumerGroup = GROUP_STAT_USER)
public class UserStatisticConsumer implements RocketMQListener<String> {
    private static final String HASH_KEY_PREFIX = "user_statistic_";

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
                switch (UserStatisticEventEnum.valueOf(msg.getEventType())) {
                    case USER_ARTICLE:
                        tokenDao.hIncr(HASH_KEY_PREFIX + msg.getTargetId(), USER_ARTICLE.getMsg(), msg.getCount());
                        break;
                    case USER_FOLLOW:
                        tokenDao.hIncr(HASH_KEY_PREFIX + msg.getTargetId(), USER_FOLLOW.getMsg(), msg.getCount());
                        break;
                    case USER_FOLLOWER:
                        tokenDao.hIncr(HASH_KEY_PREFIX + msg.getTargetId(), USER_FOLLOWER.getMsg(), msg.getCount());
                        break;
                    case USER_FAVORITE:
                        tokenDao.hIncr(HASH_KEY_PREFIX + msg.getTargetId(), USER_FAVORITE.getMsg(), msg.getCount());
                        break;
                    case USER_ARTICLE_CANCEL:
                        tokenDao.hIncr(HASH_KEY_PREFIX + msg.getTargetId(), USER_ARTICLE.getMsg(), -msg.getCount());
                        break;
                    case USER_FOLLOW_CANCEL:
                        tokenDao.hIncr(HASH_KEY_PREFIX + msg.getTargetId(), USER_FOLLOW.getMsg(), -msg.getCount());
                        break;
                    case USER_FOLLOWER_CANCEL:
                        tokenDao.hIncr(HASH_KEY_PREFIX + msg.getTargetId(), USER_FOLLOWER.getMsg(), -msg.getCount());
                        break;
                    case USER_FAVORITE_CANCEL:
                        tokenDao.hIncr(HASH_KEY_PREFIX + msg.getTargetId(), USER_FAVORITE.getMsg(), -msg.getCount());
                        break;
                }
            } catch (Exception e) {
                log.error("UserStatistic consume error, msgId={}", msg.getMsgId(), e);
            }
        });
    }
}

package com.example.pai_demo.rocketmq.consumer;

import com.alibaba.fastjson.JSON;
import com.example.pai_demo.dao.TokenDao;
import com.example.pai_demo.enums.ArticleStatisticEventEnum;
import com.example.pai_demo.enums.CommentStatisticEventEnum;
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

import static com.example.pai_demo.enums.ArticleStatisticEventEnum.*;
import static com.example.pai_demo.enums.CommentStatisticEventEnum.COMMENT_LIKE;
import static com.example.pai_demo.enums.UserStatisticEventEnum.*;
import static com.example.pai_demo.rocketmq.RocketMQTopicConfig.GROUP_STAT_MESSAGE;
import static com.example.pai_demo.rocketmq.RocketMQTopicConfig.TOPIC_STAT_MESSAGE;

/**
 * 统计消息消费者（合并 user / article / comment 三类）
 * 消费 pai-stat-message 消息，按 eventType 路由到不同 Redis 写操作
 *
 * @author GilbertYoung
 * @date 2026/08/07 10:00
 */
@Component
@Slf4j
@ConditionalOnProperty(name = "rocketmq.consumer.enabled", havingValue = "true")
@RocketMQMessageListener(topic = TOPIC_STAT_MESSAGE, consumerGroup = GROUP_STAT_MESSAGE)
public class StatMessageConsumer implements RocketMQListener<String> {
    private static final String USER_PREFIX = "user_statistic_";
    private static final String ARTICLE_PREFIX = "article_statistic_";
    private static final String COMMENT_PREFIX = "comment_statistic_";

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
            String type = msg.getEventType();
            try {
                if (type.startsWith("USER_")) {
                    handleUser(msg, UserStatisticEventEnum.valueOf(type));
                } else if (type.startsWith("ARTICLE_")) {
                    handleArticle(msg, ArticleStatisticEventEnum.valueOf(type));
                } else if (type.startsWith("COMMENT_")) {
                    handleComment(msg, CommentStatisticEventEnum.valueOf(type));
                }
            } catch (Exception e) {
                log.error("StatMessage consume error, msgId={}", msg.getMsgId(), e);
            }
        });
    }

    private void handleUser(StatEventMessage msg, UserStatisticEventEnum type) {
        switch (type) {
            case USER_ARTICLE:
                tokenDao.hIncr(USER_PREFIX + msg.getTargetId(), USER_ARTICLE.getMsg(), msg.getCount());
                break;
            case USER_FOLLOW:
                tokenDao.hIncr(USER_PREFIX + msg.getTargetId(), USER_FOLLOW.getMsg(), msg.getCount());
                break;
            case USER_FOLLOWER:
                tokenDao.hIncr(USER_PREFIX + msg.getTargetId(), USER_FOLLOWER.getMsg(), msg.getCount());
                break;
            case USER_FAVORITE:
                tokenDao.hIncr(USER_PREFIX + msg.getTargetId(), USER_FAVORITE.getMsg(), msg.getCount());
                break;
            case USER_ARTICLE_CANCEL:
                tokenDao.hIncr(USER_PREFIX + msg.getTargetId(), USER_ARTICLE.getMsg(), -msg.getCount());
                break;
            case USER_FOLLOW_CANCEL:
                tokenDao.hIncr(USER_PREFIX + msg.getTargetId(), USER_FOLLOW.getMsg(), -msg.getCount());
                break;
            case USER_FOLLOWER_CANCEL:
                tokenDao.hIncr(USER_PREFIX + msg.getTargetId(), USER_FOLLOWER.getMsg(), -msg.getCount());
                break;
            case USER_FAVORITE_CANCEL:
                tokenDao.hIncr(USER_PREFIX + msg.getTargetId(), USER_FAVORITE.getMsg(), -msg.getCount());
                break;
        }
    }

    private void handleArticle(StatEventMessage msg, ArticleStatisticEventEnum type) {
        switch (type) {
            case ARTICLE_READ:
                tokenDao.hIncr(ARTICLE_PREFIX + msg.getTargetId(), ARTICLE_READ.getMsg(), msg.getCount());
                break;
            case ARTICLE_LIKE:
                tokenDao.hIncr(ARTICLE_PREFIX + msg.getTargetId(), ARTICLE_LIKE.getMsg(), msg.getCount());
                break;
            case ARTICLE_COMMENT:
                tokenDao.hIncr(ARTICLE_PREFIX + msg.getTargetId(), ARTICLE_COMMENT.getMsg(), msg.getCount());
                break;
            case ARTICLE_FAVORITE:
                tokenDao.hIncr(ARTICLE_PREFIX + msg.getTargetId(), ARTICLE_FAVORITE.getMsg(), msg.getCount());
                break;
            case ARTICLE_LIKE_CANCEL:
                tokenDao.hIncr(ARTICLE_PREFIX + msg.getTargetId(), ARTICLE_LIKE.getMsg(), -msg.getCount());
                break;
            case ARTICLE_COMMENT_CANCEL:
                tokenDao.hIncr(ARTICLE_PREFIX + msg.getTargetId(), ARTICLE_COMMENT.getMsg(), -msg.getCount());
                break;
            case ARTICLE_FAVORITE_CANCEL:
                tokenDao.hIncr(ARTICLE_PREFIX + msg.getTargetId(), ARTICLE_FAVORITE.getMsg(), -msg.getCount());
                break;
        }
    }

    private void handleComment(StatEventMessage msg, CommentStatisticEventEnum type) {
        switch (type) {
            case COMMENT_LIKE:
                tokenDao.hIncr(COMMENT_PREFIX + msg.getTargetId(), COMMENT_LIKE.getMsg(), msg.getCount());
                break;
            case COMMENT_LIKE_CANCEL:
                tokenDao.hIncr(COMMENT_PREFIX + msg.getTargetId(), COMMENT_LIKE.getMsg(), -msg.getCount());
                break;
        }
    }
}

package com.example.pai_demo.rocketmq.consumer;

import com.alibaba.fastjson.JSON;
import com.example.pai_demo.dao.TokenDao;
import com.example.pai_demo.enums.ArticleStatisticEventEnum;
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
import static com.example.pai_demo.rocketmq.RocketMQTopicConfig.GROUP_STAT_ARTICLE;
import static com.example.pai_demo.rocketmq.RocketMQTopicConfig.TOPIC_STAT_ARTICLE;

/**
 * 文章统计消费者（取代 ArticleStatisticEventListener）
 * 消费 paistat-article 消息，异步更新 Redis 文章维度统计
 *
 * @author GilbertYoung
 * @date 2026/07/25 10:00
 */
@Component
@Slf4j
@ConditionalOnProperty(name = "rocketmq.consumer.enabled", havingValue = "true")
@RocketMQMessageListener(topic = TOPIC_STAT_ARTICLE, consumerGroup = GROUP_STAT_ARTICLE)
public class ArticleStatisticConsumer implements RocketMQListener<String> {
    private static final String HASH_KEY_PREFIX = "article_statistic_";

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
                switch (ArticleStatisticEventEnum.valueOf(msg.getEventType())) {
                    case ARTICLE_READ:
                        tokenDao.hIncr(HASH_KEY_PREFIX + msg.getTargetId(), ARTICLE_READ.getMsg(), 1);
                        break;
                    case ARTICLE_LIKE:
                        tokenDao.hIncr(HASH_KEY_PREFIX + msg.getTargetId(), ARTICLE_LIKE.getMsg(), 1);
                        break;
                    case ARTICLE_COMMENT:
                        tokenDao.hIncr(HASH_KEY_PREFIX + msg.getTargetId(), ARTICLE_COMMENT.getMsg(), 1);
                        break;
                    case ARTICLE_FAVORITE:
                        tokenDao.hIncr(HASH_KEY_PREFIX + msg.getTargetId(), ARTICLE_FAVORITE.getMsg(), 1);
                        break;
                    case ARTICLE_LIKE_CANCEL:
                        tokenDao.hIncr(HASH_KEY_PREFIX + msg.getTargetId(), ARTICLE_LIKE.getMsg(), -1);
                        break;
                    case ARTICLE_COMMENT_CANCEL:
                        tokenDao.hIncr(HASH_KEY_PREFIX + msg.getTargetId(), ARTICLE_COMMENT.getMsg(), -1);
                        break;
                    case ARTICLE_FAVORITE_CANCEL:
                        tokenDao.hIncr(HASH_KEY_PREFIX + msg.getTargetId(), ARTICLE_FAVORITE.getMsg(), -1);
                        break;
                }
            } catch (Exception e) {
                log.error("ArticleStatistic consume error, msgId={}", msg.getMsgId(), e);
            }
        });
    }
}

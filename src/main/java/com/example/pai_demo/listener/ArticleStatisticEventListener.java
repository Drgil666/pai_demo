package com.example.pai_demo.listener;

import com.alibaba.fastjson.JSON;
import com.example.pai_demo.dao.TokenDao;
import com.example.pai_demo.model.event.ArticleStatisticEvent;
import com.example.pai_demo.model.message.StatEventMessage;
import com.example.pai_demo.utils.SnowflakeIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.example.pai_demo.enums.ArticleStatisticEventEnum.*;
import static com.example.pai_demo.rocketmq.RocketMQTopicConfig.TOPIC_STAT_ARTICLE;

@Component
@Slf4j
public class ArticleStatisticEventListener {
    private static final int RETRY_TIMES = 3;
    private static final long RETRY_DELAY_MS = 2000;
    private static final String HASH_KEY_PREFIX = "article_statistic_";

    @Resource
    private RocketMQTemplate rocketMQTemplate;
    @Resource
    private TokenDao tokenDao;

    @EventListener(classes = ArticleStatisticEvent.class)
    @Async
    public void articleStatisticEventListener(ArticleStatisticEvent event) {
        StatEventMessage msg = new StatEventMessage(SnowflakeIdUtil.generateId(),
                event.getType().name(), event.getArticleId());
        for (int i = 0; i < RETRY_TIMES; i++) {
            try {
                rocketMQTemplate.convertAndSend(TOPIC_STAT_ARTICLE, msg);
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
        int delta = event.getType().name().endsWith("_CANCEL") ? -1 : 1;
        switch (event.getType()) {
            case ARTICLE_READ:
                tokenDao.hIncr(HASH_KEY_PREFIX + event.getArticleId(), ARTICLE_READ.getMsg(), 1);
                break;
            case ARTICLE_LIKE:
            case ARTICLE_LIKE_CANCEL:
                tokenDao.hIncr(HASH_KEY_PREFIX + event.getArticleId(), ARTICLE_LIKE.getMsg(), delta);
                break;
            case ARTICLE_COMMENT:
            case ARTICLE_COMMENT_CANCEL:
                tokenDao.hIncr(HASH_KEY_PREFIX + event.getArticleId(), ARTICLE_COMMENT.getMsg(), delta);
                break;
            case ARTICLE_FAVORITE:
            case ARTICLE_FAVORITE_CANCEL:
                tokenDao.hIncr(HASH_KEY_PREFIX + event.getArticleId(), ARTICLE_FAVORITE.getMsg(), delta);
                break;
        }
    }
}

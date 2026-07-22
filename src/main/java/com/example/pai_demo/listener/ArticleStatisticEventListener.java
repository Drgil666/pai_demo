package com.example.pai_demo.listener;

import com.example.pai_demo.dao.TokenDao;
import com.example.pai_demo.model.event.ArticleStatisticEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.example.pai_demo.enums.ArticleStatisticEventEnum.*;

/**
 * @author GilbertYoung
 * @date 2026/07/22 15:56
 */

/**
 * 文章相关监听类，监听阅读数、点赞数、收藏数、评论数
 */
@Component
public class ArticleStatisticEventListener {
    @Resource
    private TokenDao tokenDao;

    @EventListener(classes = ArticleStatisticEvent.class)
    @Async
    public void articleStatisticEventListener(ArticleStatisticEvent event) {
        switch (event.getType()) {
            case ARTICLE_READ:
                tokenDao.hIncr(ArticleStatisticEvent.ARTICLE_STATISTIC_EVENT_PREFIX + event.getArticleId(), ARTICLE_READ.getMsg(), 1);
                break;
            case ARTICLE_LIKE:
                tokenDao.hIncr(ArticleStatisticEvent.ARTICLE_STATISTIC_EVENT_PREFIX + event.getArticleId(), ARTICLE_LIKE.getMsg(), 1);
                break;
            case ARTICLE_COMMENT:
                tokenDao.hIncr(ArticleStatisticEvent.ARTICLE_STATISTIC_EVENT_PREFIX + event.getArticleId(), ARTICLE_COMMENT.getMsg(), 1);
                break;
            case ARTICLE_FAVORITE:
                tokenDao.hIncr(ArticleStatisticEvent.ARTICLE_STATISTIC_EVENT_PREFIX + event.getArticleId(), ARTICLE_FAVORITE.getMsg(), 1);
                break;
            case ARTICLE_LIKE_CANCEL:
                tokenDao.hIncr(ArticleStatisticEvent.ARTICLE_STATISTIC_EVENT_PREFIX + event.getArticleId(), ARTICLE_LIKE.getMsg(), -1);
                break;
            case ARTICLE_COMMENT_CANCEL:
                tokenDao.hIncr(ArticleStatisticEvent.ARTICLE_STATISTIC_EVENT_PREFIX + event.getArticleId(), ARTICLE_COMMENT.getMsg(), -1);
                break;
            case ARTICLE_FAVORITE_CANCEL:
                tokenDao.hIncr(ArticleStatisticEvent.ARTICLE_STATISTIC_EVENT_PREFIX + event.getArticleId(), ARTICLE_FAVORITE.getMsg(), -1);
                break;
            default:
        }
    }

}

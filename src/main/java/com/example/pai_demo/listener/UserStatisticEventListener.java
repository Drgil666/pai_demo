package com.example.pai_demo.listener;

/**
 * @author GilbertYoung
 * @date 2026/07/22 14:54
 */

import com.example.pai_demo.dao.TokenDao;
import com.example.pai_demo.model.event.UserStatisticEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.example.pai_demo.enums.UserStatisticEventEnum.*;

/**
 * 用户相关监听类，监听发表文章数、关注数、粉丝数、收藏文章数
 */
@Component
public class UserStatisticEventListener {
    @Resource
    private TokenDao tokenDao;

    @EventListener(classes = UserStatisticEvent.class)
    @Async
    public void userStatisticEventListener(UserStatisticEvent event) {
        switch (event.getType()) {
            case USER_ARTICLE:
                tokenDao.hIncr(UserStatisticEvent.USER_STATISTIC_EVENT_PREFIX + event.getUserId(), USER_ARTICLE.getMsg(), 1);
                break;
            case USER_FOLLOW:
                tokenDao.hIncr(UserStatisticEvent.USER_STATISTIC_EVENT_PREFIX + event.getUserId(), USER_FOLLOW.getMsg(), 1);
                break;
            case USER_FOLLOWER:
                tokenDao.hIncr(UserStatisticEvent.USER_STATISTIC_EVENT_PREFIX + event.getUserId(), USER_FOLLOWER.getMsg(), 1);
                break;
            case USER_FAVORITE:
                tokenDao.hIncr(UserStatisticEvent.USER_STATISTIC_EVENT_PREFIX + event.getUserId(), USER_FAVORITE.getMsg(), 1);
                break;
            case USER_ARTICLE_CANCEL:
                tokenDao.hIncr(UserStatisticEvent.USER_STATISTIC_EVENT_PREFIX + event.getUserId(), USER_ARTICLE.getMsg(), -1);
                break;
            case USER_FOLLOW_CANCEL:
                tokenDao.hIncr(UserStatisticEvent.USER_STATISTIC_EVENT_PREFIX + event.getUserId(), USER_FOLLOW.getMsg(), -1);
                break;
            case USER_FOLLOWER_CANCEL:
                tokenDao.hIncr(UserStatisticEvent.USER_STATISTIC_EVENT_PREFIX + event.getUserId(), USER_FOLLOWER.getMsg(), -1);
                break;
            case USER_FAVORITE_CANCEL:
                tokenDao.hIncr(UserStatisticEvent.USER_STATISTIC_EVENT_PREFIX + event.getUserId(), USER_FAVORITE.getMsg(), -1);
                break;
            default:
        }
    }
}

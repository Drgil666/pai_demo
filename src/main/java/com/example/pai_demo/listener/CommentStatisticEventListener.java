package com.example.pai_demo.listener;

import com.example.pai_demo.dao.TokenDao;
import com.example.pai_demo.model.event.CommentStatisticEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.example.pai_demo.enums.CommentStatisticEventEnum.COMMENT_LIKE;

/**
 * @author GilbertYoung
 * @date 2026/07/22 15:56
 */

/**
 * 评论相关监听类，监听点赞数
 */
@Component
public class CommentStatisticEventListener {
    @Resource
    private TokenDao tokenDao;

    @EventListener(classes = CommentStatisticEvent.class)
    @Async
    public void articleStatisticEventListener(CommentStatisticEvent event) {
        switch (event.getType()) {
            case COMMENT_LIKE:
                tokenDao.hIncr(CommentStatisticEvent.COMMENT_STATISTIC_EVENT_PREFIX + event.getCommentId(), COMMENT_LIKE.getMsg(), 1);
                break;
            case COMMENT_LIKE_CANCEL:
                tokenDao.hIncr(CommentStatisticEvent.COMMENT_STATISTIC_EVENT_PREFIX + event.getCommentId(), COMMENT_LIKE.getMsg(), -1);
                break;
            default:
        }
    }
}

package com.example.pai_demo.service.impl;

import com.example.pai_demo.enums.ArticleStatisticEventEnum;
import com.example.pai_demo.mapper.CommentMapper;
import com.example.pai_demo.model.Comment;
import com.example.pai_demo.model.event.ArticleStatisticEvent;
import com.example.pai_demo.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author GilbertYoung
 * @date 2026/07/22 14:00
 */
@Service
@Slf4j
public class CommentServiceImpl implements CommentService {
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private ApplicationEventPublisher eventPublisher;

    /**
     * 创建评论
     *
     * @param comment 评论
     * @return 是否创建成功
     */
    @Override
    public Boolean createComment(Comment comment) {
        comment.setIsDelete(0);
        comment.setCreateTime(new Date());
        comment.setUpdateTime(comment.getCreateTime());
        if (commentMapper.createComment(comment)) {
            //创建评论时，同步更新缓存
            ArticleStatisticEvent articleStatisticEvent = new ArticleStatisticEvent();
            articleStatisticEvent.setArticleId(comment.getArticleId());
            articleStatisticEvent.setType(ArticleStatisticEventEnum.ARTICLE_COMMENT);
            eventPublisher.publishEvent(articleStatisticEvent);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 增量更新评论
     *
     * @param comment 评论
     * @return 影响的行数
     */
    @Override
    public Long updateCommentSelective(Comment comment) {
        Comment backup = commentMapper.getCommentById(comment.getId());
        comment.setUpdateTime(new Date());
        Long result = commentMapper.updateCommentSelective(comment);
        if (result != 0) {
            if (comment.getIsDelete() == 1) {
                Long cnt = commentMapper.deleteCommentsByTopCommentId(comment.getId());
                //评论被删除时,所有的子评论都要被删除,并且文章的缓存要同步更新
                ArticleStatisticEvent articleStatisticEvent = new ArticleStatisticEvent();
                articleStatisticEvent.setArticleId(backup.getArticleId());
                articleStatisticEvent.setType(ArticleStatisticEventEnum.ARTICLE_COMMENT_CANCEL);
                for (long i = 0L; i <= cnt; i++) {
                    eventPublisher.publishEvent(articleStatisticEvent);
                }
            }
            return result;
        } else {
            return 0L;
        }
    }

    /**
     * 全量更新评论
     *
     * @param comment 评论
     * @return 影响的行数
     */
    @Override
    public Long updateCommentAll(Comment comment) {
        Comment backup = commentMapper.getCommentById(comment.getId());
        comment.setUpdateTime(new Date());
        Long result = commentMapper.updateCommentAll(comment);
        if (result != 0) {
            comment = commentMapper.getCommentById(comment.getId());
            if (comment.getIsDelete() == 1) {
                Long cnt = commentMapper.deleteCommentsByTopCommentId(comment.getId());
                //评论被删除时,所有的子评论都要被删除,并且文章的缓存要同步更新
                ArticleStatisticEvent articleStatisticEvent = new ArticleStatisticEvent();
                articleStatisticEvent.setArticleId(backup.getArticleId());
                articleStatisticEvent.setType(ArticleStatisticEventEnum.ARTICLE_COMMENT_CANCEL);
                for (long i = 0L; i < cnt; i++) {
                    eventPublisher.publishEvent(articleStatisticEvent);
                }
            }
            return result;
        } else {
            return 0L;
        }
    }

    /**
     * 根据id获取评论
     *
     * @param id 评论id
     * @return 评论信息
     */
    @Override
    public Comment getCommentById(Integer id) {
        return commentMapper.getCommentById(id);
    }

    /**
     * 根据文章id获取评论列表
     *
     * @param articleId 文章id
     * @param keyword   关键词
     * @return 评论列表
     */
    @Override
    public List<Comment> getCommentListByArticleId(Integer articleId, String keyword) {
        return commentMapper.getCommentListByArticleId(articleId, keyword);
    }

    /**
     * 根据顶级评论id获取子评论列表
     *
     * @param topCommentId 顶级评论id
     * @return 评论列表
     */
    @Override
    public Long deleteCommentsByTopCommentId(Integer topCommentId) {
        return commentMapper.deleteCommentsByTopCommentId(topCommentId);
    }

    @Override
    public List<Comment> getCommentListByTopCommentId(Integer topCommentId, String keyword) {
        return commentMapper.getCommentListByTopCommentId(topCommentId, keyword);
    }
}

package com.example.pai_demo.service.impl;

import com.example.pai_demo.mapper.CommentMapper;
import com.example.pai_demo.model.Comment;
import com.example.pai_demo.service.CommentService;
import lombok.extern.slf4j.Slf4j;
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
        return commentMapper.createComment(comment);
    }

    /**
     * 增量更新评论
     *
     * @param comment 评论
     * @return 影响的行数
     */
    @Override
    public Long updateCommentSelective(Comment comment) {
        comment.setUpdateTime(new Date());
        return commentMapper.updateCommentSelective(comment);
    }

    /**
     * 全量更新评论
     *
     * @param comment 评论
     * @return 影响的行数
     */
    @Override
    public Long updateCommentAll(Comment comment) {
        comment.setUpdateTime(new Date());
        return commentMapper.updateCommentAll(comment);
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
     * @param keyword      关键词
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

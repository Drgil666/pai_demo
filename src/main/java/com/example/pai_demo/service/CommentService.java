package com.example.pai_demo.service;

import com.example.pai_demo.model.Comment;
import com.example.pai_demo.model.vo.CommentVO;

import java.util.List;

/**
 * @author GilbertYoung
 * @date 2026/07/22 14:00
 */
public interface CommentService {
    /**
     * 创建评论
     *
     * @param comment 评论
     * @return 是否创建成功
     */
    Boolean createComment(Comment comment);

    /**
     * 增量更新评论
     *
     * @param comment 评论
     * @return 影响的行数
     */
    Long updateCommentSelective(Comment comment);

    /**
     * 全量更新评论
     *
     * @param comment 评论
     * @return 影响的行数
     */
    Long updateCommentAll(Comment comment);

    /**
     * 根据id获取评论
     *
     * @param id 评论id
     * @return 评论信息
     */
    Comment getCommentById(Integer id);

    /**
     * 根据id获取评论vo类
     *
     * @param id 评论id
     * @return 评论信息
     */
    CommentVO getCommentVOById(Integer id);

    /**
     * 根据文章id获取评论列表
     *
     * @param articleId 文章id
     * @param keyword   关键词
     * @return 评论列表
     */
    List<CommentVO> getCommentListByArticleId(Integer articleId, String keyword);

    /**
     * 根据顶级评论id获取子评论列表
     *
     * @param topCommentId 顶级评论id
     * @param keyword      关键词
     * @return 评论列表
     */
    /**
     * 根据顶级评论id删除其下所有子评论（软删除）
     *
     * @param topCommentId 顶级评论id
     * @return 影响的行数
     */
    Long deleteCommentsByTopCommentId(Integer topCommentId);

    List<CommentVO> getCommentListByTopCommentId(Integer topCommentId, String keyword);
}

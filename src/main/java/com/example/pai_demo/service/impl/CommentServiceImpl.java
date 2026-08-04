package com.example.pai_demo.service.impl;

import com.example.pai_demo.dao.TokenDao;
import com.example.pai_demo.mapper.CommentMapper;
import com.example.pai_demo.model.Comment;
import com.example.pai_demo.model.event.CommentStatisticEvent;
import com.example.pai_demo.model.vo.CommentVO;
import com.example.pai_demo.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.pai_demo.enums.CommentStatisticEventEnum.COMMENT_LIKE;

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
    private TokenDao tokenDao;

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
    public List<CommentVO> getCommentListByArticleId(Integer articleId, String keyword) {
        List<CommentVO> commentVOList = new ArrayList<>();
        List<Comment> commentList = commentMapper.getCommentListByArticleId(articleId, keyword);
        for (Comment comment : commentList) {
            CommentVO commentVO = getCommentVO(comment, keyword);
            commentVOList.add(commentVO);
        }
        return commentVOList;
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
    public List<CommentVO> getCommentListByTopCommentId(Integer topCommentId, String keyword) {
        List<CommentVO> commentVOList = new ArrayList<>();
        List<Comment> commentList = commentMapper.getCommentListByTopCommentId(topCommentId, keyword);
        for (Comment comment : commentList) {
            CommentVO commentVO = getCommentVO(comment, keyword);
            commentVOList.add(commentVO);
        }
        return commentVOList;
    }

    @NotNull
    private CommentVO getCommentVO(Comment comment, String keyword) {
        CommentVO commentVO = new CommentVO();
        BeanUtils.copyProperties(commentVO, commentVO);
        Long likeCount = tokenDao.hScore(CommentStatisticEvent.COMMENT_STATISTIC_EVENT_PREFIX + comment.getId(), COMMENT_LIKE.getMsg());
        commentVO.setLikeCount(likeCount);
        return commentVO;
    }
}

package com.example.pai_demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.pai_demo.dao.EsQueryDao;
import com.example.pai_demo.dao.TokenDao;
import com.example.pai_demo.mapper.CommentMapper;
import com.example.pai_demo.model.Comment;
import com.example.pai_demo.model.elastic_search.CommentDocument;
import com.example.pai_demo.model.event.CommentStatisticEvent;
import com.example.pai_demo.model.vo.CommentVO;
import com.example.pai_demo.service.CommentService;
import com.example.pai_demo.utils.ListPageUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.pai_demo.enums.CommentStatisticEventEnum.COMMENT_LIKE;

/**
 * @author GilbertYoung
 * @date 2026/07/22 14:00
 */

/**
 * 为Comment相关设置redis缓存
 * 在创建更新时,先写入mysql,然后删除redis
 * 在单个读取时更新缓存，在列表获取时顺便更新单点缓存，保证PageHelper不失效
 */
@Service
@Slf4j
public class CommentServiceImpl implements CommentService {
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private TokenDao tokenDao;
    public static final String REDIS_COMMENT_ID_KEY_PREFIX = "comment:id:";
    @Value("${redis.expire.commentTime}")
    private Long commentExpireTime;
    @Resource
    private EsQueryDao esQueryDao;
    @Value("${elasticsearch.enabled:false}")
    private Boolean esEnabled;
    /**
     * 创建评论
     *
     * @param comment 评论
     * @return 是否创建成功
     */

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createComment(Comment comment) {
        comment.setIsDelete(0);
        comment.setCreateTime(new Date());
        comment.setUpdateTime(comment.getCreateTime());
        if (commentMapper.createComment(comment)) {
            String redisKey = REDIS_COMMENT_ID_KEY_PREFIX + comment.getId();
            tokenDao.deleteValue(redisKey);
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
    @Transactional(rollbackFor = Exception.class)
    public Long updateCommentSelective(Comment comment) {
        Comment old = commentMapper.getCommentById(comment.getId());
        comment.setUpdateTime(new Date());
        Long result = commentMapper.updateCommentSelective(comment);
        if (result == 1) {
            String redisKey = REDIS_COMMENT_ID_KEY_PREFIX + old.getId();
            tokenDao.deleteValue(redisKey);
        }
        return result;
    }

    /**
     * 全量更新评论
     *
     * @param comment 评论
     * @return 影响的行数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long updateCommentAll(Comment comment) {
        Comment old = commentMapper.getCommentById(comment.getId());
        comment.setUpdateTime(new Date());
        Long result = commentMapper.updateCommentAll(comment);
        if (result == 1) {
            String redisKey = REDIS_COMMENT_ID_KEY_PREFIX + old.getId();
            tokenDao.deleteValue(redisKey);
        }
        return result;
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
     * 根据id获取评论vo类
     *
     * @param id 评论id
     * @return 评论信息
     */
    @Override
    public CommentVO getCommentVOById(Integer id) {
        String redisKey = REDIS_COMMENT_ID_KEY_PREFIX + id;
        String redisValue = tokenDao.getValue(redisKey);
        if (redisValue != null) {
            return JSONObject.parseObject(redisValue, CommentVO.class);
        }
        Comment comment = getCommentById(id);
        CommentVO commentVO = getCommentVO(comment);
        tokenDao.setValue(redisKey, JSONObject.toJSONString(commentVO), commentExpireTime);
        return commentVO;
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
        List<Comment> commentList;
        if (Boolean.TRUE.equals(esEnabled)) {
            int from = ListPageUtil.pageFrom();
            int size = ListPageUtil.pageSize();
            String orderField = ListPageUtil.orderField();
            boolean asc = ListPageUtil.orderAsc();
            ListPageUtil.clearPage();
            commentList = toComments(esQueryDao.findCommentsByArticleId(articleId, keyword, from, size, orderField, asc));
        } else {
            commentList = commentMapper.getCommentListByArticleId(articleId, keyword);
        }
        List<CommentVO> commentVOList = new ArrayList<>();
        for (Comment comment : commentList) {
            CommentVO commentVO = getCommentVO(comment);
            String redisKey = REDIS_COMMENT_ID_KEY_PREFIX + comment.getId();
            tokenDao.setValue(redisKey, JSONObject.toJSONString(commentVO), commentExpireTime);
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
        List<Comment> commentList;
        if (Boolean.TRUE.equals(esEnabled)) {
            int from = ListPageUtil.pageFrom();
            int size = ListPageUtil.pageSize();
            String orderField = ListPageUtil.orderField();
            boolean asc = ListPageUtil.orderAsc();
            ListPageUtil.clearPage();
            commentList = toComments(esQueryDao.findCommentsByTopCommentId(topCommentId, keyword, from, size, orderField, asc));
        } else {
            commentList = commentMapper.getCommentListByTopCommentId(topCommentId, keyword);
        }
        List<CommentVO> commentVOList = new ArrayList<>();
        for (Comment comment : commentList) {
            CommentVO commentVO = getCommentVO(comment);
            String redisKey = REDIS_COMMENT_ID_KEY_PREFIX + comment.getId();
            tokenDao.setValue(redisKey, JSONObject.toJSONString(commentVO), commentExpireTime);
            commentVOList.add(commentVO);
        }
        return commentVOList;
    }

    private List<Comment> toComments(List<CommentDocument> documents) {
        List<Comment> comments = new ArrayList<>();
        for (CommentDocument document : documents) {
            Comment comment = new Comment();
            BeanUtils.copyProperties(document, comment);
            comments.add(comment);
        }
        return comments;
    }

    @NotNull
    private CommentVO getCommentVO(Comment comment) {
        CommentVO commentVO = new CommentVO();
        BeanUtils.copyProperties(comment, commentVO);
        Long likeCount = tokenDao.hScore(CommentStatisticEvent.COMMENT_STATISTIC_EVENT_PREFIX + comment.getId(), COMMENT_LIKE.getMsg());
        commentVO.setLikeCount(likeCount);
        return commentVO;
    }
}

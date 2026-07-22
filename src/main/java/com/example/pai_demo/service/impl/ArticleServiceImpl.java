package com.example.pai_demo.service.impl;

import com.example.pai_demo.enums.ArticleStatisticEventEnum;
import com.example.pai_demo.enums.UserStatisticEventEnum;
import com.example.pai_demo.mapper.ArticleMapper;
import com.example.pai_demo.mapper.ArticleTagMapper;
import com.example.pai_demo.mapper.CommentMapper;
import com.example.pai_demo.mapper.UserFavoriteMapper;
import com.example.pai_demo.model.Article;
import com.example.pai_demo.model.Tag;
import com.example.pai_demo.model.event.ArticleStatisticEvent;
import com.example.pai_demo.model.event.UserStatisticEvent;
import com.example.pai_demo.model.vo.ArticleVO;
import com.example.pai_demo.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author GilbertYoung
 * @date 2026/07/13 20:11
 */
@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {
    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private ArticleTagMapper articleTagMapper;
    @Resource
    private UserFavoriteMapper userFavoriteMapper;
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private ApplicationEventPublisher eventPublisher;

    /**
     * 创建文章
     *
     * @param article 创建的文章
     * @return 是否创建成功
     */
    @Override
    public Boolean createArticle(Article article) {
        article.setIsDelete(0);
        article.setStatus(0);
        article.setCreateTime(new Date());
        article.setUpdateTime(article.getCreateTime());
        if (articleMapper.createArticle(article)) {
            //创建文章时redis同步缓存
            UserStatisticEvent userStatisticEvent = new UserStatisticEvent();
            userStatisticEvent.setUserId(article.getUserId());
            userStatisticEvent.setType(UserStatisticEventEnum.USER_ARTICLE);
            eventPublisher.publishEvent(userStatisticEvent);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 增量更新文章
     *
     * @param article 更新的文章
     * @return 影响的行数
     */
    @Override
    public Long updateArticleSelective(Article article) {
        Article backup = articleMapper.getArticleById(article.getId());
        article.setUpdateTime(new Date());
        Long result = articleMapper.updateArticleSelective(article);
        if (result != 0) {
            if (article.getIsDelete() == 1) {
                //删除文章时,修改对应数据
                UserStatisticEvent userStatisticEvent = new UserStatisticEvent();
                userStatisticEvent.setUserId(backup.getUserId());
                userStatisticEvent.setType(UserStatisticEventEnum.USER_ARTICLE_CANCEL);
                eventPublisher.publishEvent(userStatisticEvent);
            }
            return result;
        } else {
            return 0L;
        }
    }

    /**
     * 全量更新
     *
     * @param article 更新的文章
     * @return 影响的行数
     */
    @Override
    public Long updateArticleAll(Article article) {
        Article backup = articleMapper.getArticleById(article.getId());
        article.setUpdateTime(new Date());
        Long result = articleMapper.updateArticleAll(article);
        if (result != 0) {
            article = articleMapper.getArticleById(article.getId());
            if (article.getIsDelete() == 1) {
                //删除文章时,修改对应数据
                UserStatisticEvent userStatisticEvent = new UserStatisticEvent();
                userStatisticEvent.setUserId(backup.getUserId());
                userStatisticEvent.setType(UserStatisticEventEnum.USER_ARTICLE_CANCEL);
                eventPublisher.publishEvent(userStatisticEvent);
            }
            return result;
        } else {
            return 0L;
        }
    }

    /**
     * 根据id获取文章
     *
     * @param id 文章id
     * @return 文章信息
     */
    @Override
    public ArticleVO getArticleVOById(Integer id) {
        Article article = articleMapper.getArticleById(id);
        ArticleVO articleVO = getArticleVO(article, null);
        //阅读文章时redis同步缓存
        ArticleStatisticEvent articleStatisticEvent = new ArticleStatisticEvent();
        articleStatisticEvent.setArticleId(article.getId());
        articleStatisticEvent.setType(ArticleStatisticEventEnum.ARTICLE_READ);
        eventPublisher.publishEvent(articleStatisticEvent);
        return articleVO;
    }

    /**
     * 根据id获取文章
     *
     * @param id 文章id
     * @return 文章信息
     */
    @Override
    public Article getArticleById(Integer id) {
        return articleMapper.getArticleById(id);
    }

    /**
     * 根据用户id获取文章列表
     *
     * @param userId  用户id
     * @param keyword 关键词
     * @return 文章列表
     */
    @Override
    public List<ArticleVO> getArticleVOListByUserId(Integer userId, String keyword) {
        List<Article> articleList = articleMapper.getArticleListByUserId(userId, keyword);
        List<ArticleVO> articleVOList = new ArrayList<>();
        for (Article article : articleList) {
            ArticleVO articleVO = getArticleVO(article, keyword);
            articleVOList.add(articleVO);
        }
        return articleVOList;
    }

    /**
     * 根据目录id获取文章列表
     *
     * @param categoryId 目录id
     * @param keyword    关键词
     * @return 文章列表
     */
    @Override
    public List<ArticleVO> getArticleVOListByCategoryId(Integer categoryId, String keyword) {
        List<Article> articleList = articleMapper.getArticleListByCategoryId(categoryId, keyword);
        List<ArticleVO> articleVOList = new ArrayList<>();
        for (Article article : articleList) {
            ArticleVO articleVO = getArticleVO(article, keyword);
            articleVOList.add(articleVO);
        }
        return articleVOList;
    }

    @NotNull
    private ArticleVO getArticleVO(Article article, String keyword) {
        ArticleVO articleVO = new ArticleVO();
        BeanUtils.copyProperties(article, articleVO);
        List<Tag> tagList = articleTagMapper.getTagListByArticleId(article.getId(), keyword);
        articleVO.setArticleTag(tagList);
        articleVO.setFavoriteCount(userFavoriteMapper.getUserFavoriteCountByArticleId(article.getId()));
        articleVO.setCommentCount(commentMapper.getCommentCountByArticleId(article.getId()));
        return articleVO;
    }
}

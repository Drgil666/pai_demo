package com.example.pai_demo.service.impl;

import com.example.pai_demo.mapper.ArticleMapper;
import com.example.pai_demo.mapper.ArticleTagMapper;
import com.example.pai_demo.mapper.CommentMapper;
import com.example.pai_demo.mapper.UserFavoriteMapper;
import com.example.pai_demo.model.Article;
import com.example.pai_demo.model.Tag;
import com.example.pai_demo.model.vo.ArticleVO;
import com.example.pai_demo.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
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
        return articleMapper.createArticle(article);
    }

    /**
     * 增量更新文章
     *
     * @param article 更新的文章
     * @return 影响的行数
     */
    @Override
    public Long updateArticleSelective(Article article) {
        article.setUpdateTime(new Date());
        return articleMapper.updateArticleSelective(article);
    }

    /**
     * 全量更新
     *
     * @param article 更新的文章
     * @return 影响的行数
     */
    @Override
    public Long updateArticleAll(Article article) {
        article.setUpdateTime(new Date());
        return articleMapper.updateArticleAll(article);
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
        return getArticleVO(articleList, keyword);
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
        return getArticleVO(articleList, keyword);
    }

    @NotNull
    private List<ArticleVO> getArticleVO(List<Article> articleList, String keyword) {
        List<ArticleVO> articleVOList = new ArrayList<>();
        for (Article article : articleList) {
            ArticleVO articleVO = new ArticleVO();
            BeanUtils.copyProperties(article, articleVO);
            List<Tag> tagList = articleTagMapper.getTagListByArticleId(article.getId(), keyword);
            articleVO.setArticleTag(tagList);
            articleVO.setFavoriteCount(userFavoriteMapper.getUserFavoriteCountByArticleId(article.getId()));
            articleVO.setCommentCount(commentMapper.getCommentCountByArticleId(article.getId()));
            articleVOList.add(articleVO);
        }
        return articleVOList;
    }
}

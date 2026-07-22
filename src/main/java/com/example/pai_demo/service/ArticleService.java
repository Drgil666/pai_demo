package com.example.pai_demo.service;

import com.example.pai_demo.model.Article;
import com.example.pai_demo.model.vo.ArticleVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author GilbertYoung
 * @date 2026/07/13 19:52
 */
public interface ArticleService {
    /**
     * 创建文章
     *
     * @param article 创建的文章
     * @return 是否创建成功
     */
    Boolean createArticle(@Param("article") Article article);

    /**
     * 增量更新文章
     *
     * @param article 更新的文章
     * @return 影响的行数
     */
    Long updateArticleSelective(Article article);

    /**
     * 全量更新
     *
     * @param article 更新的文章
     * @return 影响的行数
     */
    Long updateArticleAll(Article article);

    /**
     * 根据id获取文章
     *
     * @param id 文章id
     * @return 文章信息
     */
    ArticleVO getArticleVOById(Integer id);

    /**
     * 根据id获取文章
     *
     * @param id 文章id
     * @return 文章信息
     */
    Article getArticleById(Integer id);

    /**
     * 根据用户id获取文章列表
     *
     * @param userId  用户id
     * @param keyword 关键词
     * @return 文章列表
     */
    List<ArticleVO> getArticleVOListByUserId(Integer userId, String keyword);

    /**
     * 根据目录id获取文章列表
     *
     * @param categoryId 目录id
     * @param keyword    关键词
     * @return 文章列表
     */
    List<ArticleVO> getArticleVOListByCategoryId(Integer categoryId, String keyword);
}

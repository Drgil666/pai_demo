package com.example.pai_demo.service;

import com.example.pai_demo.model.ArticleDetail;

import java.util.List;

/**
 * @author GilbertYoung
 * @date 2026/07/21 15:17
 */
public interface ArticleDetailService {
    /**
     * 创建文章内容
     *
     * @param articleDetail 文章内容
     * @return 是否创建成功
     */
    Boolean createArticleDetail(ArticleDetail articleDetail);

    /**
     * 增量更新文章内容
     *
     * @param articleDetail 文章内容
     * @return 影响的行数
     */
    Long updateArticleDetailSelective(ArticleDetail articleDetail);

    /**
     * 全量更新文章内容
     *
     * @param articleDetail 文章内容
     * @return 影响的行数
     */
    Long updateArticleDetailAll(ArticleDetail articleDetail);

    /**
     * 根据id获取文章内容
     *
     * @param id 文章内容id
     * @return 文章内容信息
     */
    ArticleDetail getArticleDetailById(Integer id);

    /**
     * 根据文章id获取最新版本的文章内容
     *
     * @param articleId 文章id
     * @return 最新版本的文章内容
     */
    ArticleDetail getArticleDetailByArticleId(Integer articleId);

    /**
     * 根据文章id获取所有版本的文章内容列表
     *
     * @param articleId 文章id
     * @return 文章内容列表
     */
    List<ArticleDetail> getArticleDetailListByArticleId(Integer articleId);
}

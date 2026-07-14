package com.example.pai_demo.service;

import com.example.pai_demo.model.ArticleTag;
import com.example.pai_demo.model.Tag;

import java.util.List;

/**
 * @author GilbertYoung
 * @date 2026/07/14 11:03
 */
public interface ArticleTagService {
    /**
     * 创建文章标签关联
     *
     * @param articleTag 文章标签关联
     * @return 是否创建成功
     */
    Boolean createArticleTag(ArticleTag articleTag);

    /**
     * 增量更新文章标签关联
     *
     * @param articleTag 文章标签关联
     * @return 是否修改成功
     */
    Long updateArticleTagSelective(ArticleTag articleTag);

    /**
     * 全量更新文章标签关联
     *
     * @param articleTag 文章标签关联
     * @return 是否修改成功
     */
    Long updateArticleTagAll(ArticleTag articleTag);

    /**
     * 获取文章标签关联
     *
     * @param id 文章标签关联id
     * @return 文章标签关联信息
     */
    ArticleTag getArticleTagById(Integer id);

    /**
     * 根据文章id和标签id获取文章标签关联
     *
     * @param articleId 文章id
     * @param tagId     标签id
     * @return 文章标签关联
     */
    ArticleTag getArticleTagByArticleIdAndTagId(Integer articleId, Integer tagId);

    /**
     * 根据文章id获取对应的标签列表
     *
     * @param articleId 文章id
     * @param keyword   关键词
     * @return 标签列表
     */
    List<Tag> getTagListByArticleId(Integer articleId, String keyword);
}

package com.example.pai_demo.service.impl;

import com.example.pai_demo.mapper.ArticleTagMapper;
import com.example.pai_demo.model.ArticleTag;
import com.example.pai_demo.model.Tag;
import com.example.pai_demo.service.ArticleTagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author GilbertYoung
 * @date 2026/07/14 11:14
 */
@Service
@Slf4j
public class ArticleTagServiceImpl implements ArticleTagService {
    @Resource
    private ArticleTagMapper articleTagMapper;

    /**
     * 创建文章标签关联
     *
     * @param articleTag 文章标签关联
     * @return 是否创建成功
     */
    @Override
    public Boolean createArticleTag(ArticleTag articleTag) {
        articleTag.setIsDelete(0);
        articleTag.setCreateTime(new Date());
        articleTag.setUpdateTime(articleTag.getCreateTime());
        return articleTagMapper.createArticleTag(articleTag);
    }

    /**
     * 增量更新文章标签关联
     *
     * @param articleTag 文章标签关联
     * @return 是否修改成功
     */
    @Override
    public Long updateArticleTagSelective(ArticleTag articleTag) {
        articleTag.setUpdateTime(new Date());
        return articleTagMapper.updateArticleTagSelective(articleTag);
    }

    /**
     * 全量更新文章标签关联
     *
     * @param articleTag 文章标签关联
     * @return 是否修改成功
     */
    @Override
    public Long updateArticleTagAll(ArticleTag articleTag) {
        articleTag.setUpdateTime(new Date());
        return articleTagMapper.updateArticleTagAll(articleTag);
    }

    /**
     * 获取文章标签关联
     *
     * @param id 文章标签关联id
     * @return 文章标签关联信息
     */
    @Override
    public ArticleTag getArticleTagById(Integer id) {
        return articleTagMapper.getArticleTagById(id);
    }

    /**
     * 根据文章id和标签id获取文章标签关联
     *
     * @param articleId 文章id
     * @param tagId     标签id
     * @return 文章标签关联
     */
    @Override
    public ArticleTag getArticleTagByArticleIdAndTagId(Integer articleId, Integer tagId) {
        return articleTagMapper.getArticleTagByArticleIdAndTagId(articleId, tagId);
    }

    /**
     * 根据文章id获取对应的标签列表
     *
     * @param articleId 文章id
     * @param keyword   关键词
     * @return 标签列表
     */
    @Override
    public List<Tag> getTagListByArticleId(Integer articleId, String keyword) {
        return articleTagMapper.getTagListByArticleId(articleId, keyword);
    }
}

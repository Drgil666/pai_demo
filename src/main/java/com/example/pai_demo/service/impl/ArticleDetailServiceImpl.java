package com.example.pai_demo.service.impl;

import com.example.pai_demo.mapper.ArticleDetailMapper;
import com.example.pai_demo.model.ArticleDetail;
import com.example.pai_demo.service.ArticleDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author GilbertYoung
 * @date 2026/07/21 15:17
 */
@Service
@Slf4j
public class ArticleDetailServiceImpl implements ArticleDetailService {
    @Resource
    private ArticleDetailMapper articleDetailMapper;

    /**
     * 创建文章内容
     *
     * @param articleDetail 文章内容
     * @return 是否创建成功
     */
    @Override
    public Boolean createArticleDetail(ArticleDetail articleDetail) {
        articleDetail.setIsDelete(0);
        articleDetail.setCreateTime(new Date());
        articleDetail.setUpdateTime(articleDetail.getCreateTime());
        ArticleDetail articleDetail1 = articleDetailMapper.getArticleDetailByArticleId(articleDetail.getArticleId());
        if (articleDetail1 != null) {
            articleDetail.setVersion(articleDetail1.getVersion() + 1);
        } else {
            articleDetail.setVersion(1);
        }
        return articleDetailMapper.createArticleDetail(articleDetail);
    }

    /**
     * 增量更新文章内容
     *
     * @param articleDetail 文章内容
     * @return 影响的行数
     */
    @Override
    public Long updateArticleDetailSelective(ArticleDetail articleDetail) {
        articleDetail.setUpdateTime(new Date());
        return articleDetailMapper.updateArticleDetailSelective(articleDetail);
    }

    /**
     * 全量更新文章内容
     *
     * @param articleDetail 文章内容
     * @return 影响的行数
     */
    @Override
    public Long updateArticleDetailAll(ArticleDetail articleDetail) {
        articleDetail.setUpdateTime(new Date());
        return articleDetailMapper.updateArticleDetailAll(articleDetail);
    }

    /**
     * 根据id获取文章内容
     *
     * @param id 文章内容id
     * @return 文章内容信息
     */
    @Override
    public ArticleDetail getArticleDetailById(Integer id) {
        return articleDetailMapper.getArticleDetailById(id);
    }

    /**
     * 根据文章id获取最新版本的文章内容
     *
     * @param articleId 文章id
     * @return 最新版本的文章内容
     */
    @Override
    public ArticleDetail getArticleDetailByArticleId(Integer articleId) {
        return articleDetailMapper.getArticleDetailByArticleId(articleId);
    }

    /**
     * 根据文章id获取所有版本的文章内容列表
     *
     * @param articleId 文章id
     * @return 文章内容列表
     */
    @Override
    public List<ArticleDetail> getArticleDetailListByArticleId(Integer articleId) {
        return articleDetailMapper.getArticleDetailListByArticleId(articleId);
    }
}

package com.example.pai_demo.dao;

import com.example.pai_demo.model.elastic_search.ArticleDocument;
import com.example.pai_demo.model.elastic_search.CommentDocument;

import java.util.List;

/**
 * Elasticsearch 查询 DAO。
 * 数据由 Canal 从 MySQL 同步到 ES，本接口只提供查询能力。
 */
public interface EsQueryDao {
    List<ArticleDocument> findArticlesByUserId(Integer userId, String keyword, int from, int size, String orderField, boolean asc);

    List<ArticleDocument> findArticlesByCategoryId(Integer categoryId, String keyword, int from, int size, String orderField, boolean asc);

    List<CommentDocument> findCommentsByArticleId(Integer articleId, String keyword, int from, int size, String orderField, boolean asc);

    List<CommentDocument> findCommentsByTopCommentId(Integer topCommentId, String keyword, int from, int size, String orderField, boolean asc);
}

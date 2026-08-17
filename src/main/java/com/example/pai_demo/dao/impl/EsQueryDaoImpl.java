package com.example.pai_demo.dao.impl;

import com.example.pai_demo.dao.EsQueryDao;
import com.example.pai_demo.model.elastic_search.ArticleDocument;
import com.example.pai_demo.model.elastic_search.CommentDocument;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 基于 Elasticsearch 的查询 DAO 实现。
 */
@Component("EsQueryDaoImpl")
public class EsQueryDaoImpl implements EsQueryDao {
    @Resource
    private ElasticsearchOperations elasticsearchOperations;

    @Value("${elasticsearch.index.article}")
    private String articleIndex;

    @Value("${elasticsearch.index.comment}")
    private String commentIndex;

    @Override
    public List<ArticleDocument> findArticlesByUserId(Integer userId, String keyword, int from, int size, String orderField, boolean asc) {
        return search(articleIndex, "user_id", userId, "title", keyword, from, size, orderField, asc, ArticleDocument.class);
    }

    @Override
    public List<ArticleDocument> findArticlesByCategoryId(Integer categoryId, String keyword, int from, int size, String orderField, boolean asc) {
        return search(articleIndex, "category_id", categoryId, "title", keyword, from, size, orderField, asc, ArticleDocument.class);
    }

    @Override
    public List<CommentDocument> findCommentsByArticleId(Integer articleId, String keyword, int from, int size, String orderField, boolean asc) {
        return search(commentIndex, "article_id", articleId, "content", keyword, from, size, orderField, asc, CommentDocument.class);
    }

    @Override
    public List<CommentDocument> findCommentsByTopCommentId(Integer topCommentId, String keyword, int from, int size, String orderField, boolean asc) {
        return search(commentIndex, "top_comment_id", topCommentId, "content", keyword, from, size, orderField, asc, CommentDocument.class);
    }

    private <T> List<T> search(String index, String dimField, Object dimValue, String keywordField, String keyword,
                               int from, int size, String orderField, boolean asc, Class<T> clazz) {
        BoolQueryBuilder bool = QueryBuilders.boolQuery();
        bool.must(QueryBuilders.termQuery(dimField, dimValue));
        bool.must(QueryBuilders.termQuery("is_delete", 0));
        if (keyword != null && !keyword.trim().isEmpty()) {
            bool.must(QueryBuilders.wildcardQuery(keywordField, "*" + keyword + "*"));
        }
        int pageSize = Math.max(size, 1);
        int page = from / pageSize;
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(bool)
                .withPageable(PageRequest.of(page, pageSize))
                .withSort(SortBuilders.fieldSort(orderField).order(asc ? SortOrder.ASC : SortOrder.DESC))
                .build();
        SearchHits<T> hits = elasticsearchOperations.search(query, clazz, IndexCoordinates.of(index));
        return hits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());
    }
}

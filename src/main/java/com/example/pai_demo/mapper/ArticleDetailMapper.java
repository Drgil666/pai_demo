package com.example.pai_demo.mapper;

import com.example.pai_demo.model.ArticleDetail;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author GilbertYoung
 * @date 2026/07/21 15:17
 */
@Mapper
public interface ArticleDetailMapper {
    /**
     * 创建文章内容
     *
     * @param articleDetail 文章内容
     * @return 是否创建成功
     */
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    @Insert("insert into article_detail (create_time, update_time, article_id, content, version, is_delete) values " +
            "(#{articleDetail.createTime},#{articleDetail.updateTime},#{articleDetail.articleId},#{articleDetail.content},#{articleDetail.version},#{articleDetail.isDelete})")
    Boolean createArticleDetail(@Param("articleDetail") ArticleDetail articleDetail);

    /**
     * 增量更新文章内容
     *
     * @param articleDetail 文章内容
     * @return 影响的行数
     */
    Long updateArticleDetailSelective(@Param("articleDetail") ArticleDetail articleDetail);

    /**
     * 全量更新文章内容
     *
     * @param articleDetail 文章内容
     * @return 影响的行数
     */
    @Update("update article_detail set update_time=#{articleDetail.updateTime}," +
            "article_id=#{articleDetail.articleId},content=#{articleDetail.content}," +
            "version=#{articleDetail.version},is_delete=#{articleDetail.isDelete} where id=#{articleDetail.id} and is_delete=0")
    Long updateArticleDetailAll(@Param("articleDetail") ArticleDetail articleDetail);

    /**
     * 根据id获取文章内容
     *
     * @param id 文章内容id
     * @return 文章内容信息
     */
    @Select("select * from article_detail where id=#{id} and is_delete=0 LIMIT 1")
    ArticleDetail getArticleDetailById(@Param("id") Integer id);

    /**
     * 根据文章id获取最新版本的文章内容
     *
     * @param articleId 文章id
     * @return 最新版本的文章内容
     */
    @Select("select * from article_detail where article_id=#{articleId} and is_delete=0 order by version desc LIMIT 1")
    ArticleDetail getArticleDetailByArticleId(@Param("articleId") Integer articleId);

    /**
     * 根据文章id获取所有版本的文章内容列表
     *
     * @param articleId 文章id
     * @return 文章内容列表
     */
    @Select("select * from article_detail where article_id=#{articleId} and is_delete=0")
    List<ArticleDetail> getArticleDetailListByArticleId(@Param("articleId") Integer articleId);
}

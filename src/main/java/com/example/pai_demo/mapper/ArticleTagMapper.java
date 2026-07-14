package com.example.pai_demo.mapper;

import com.example.pai_demo.model.ArticleTag;
import com.example.pai_demo.model.Tag;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author GilbertYoung
 * @date 2026/07/14 11:03
 */
@Mapper
public interface ArticleTagMapper {
    /**
     * 创建文章标签关联
     *
     * @param articleTag 文章标签关联
     * @return 是否创建成功
     */
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("insert into article_tag (create_time, update_time, article_id, tag_id, is_delete) " +
            "values (#{articleTag.createTime},#{articleTag.updateTime},#{articleTag.articleId}," +
            "#{articleTag.tagId},#{articleTag.isDelete})")
    Boolean createArticleTag(@Param("articleTag") ArticleTag articleTag);

    /**
     * 增量更新文章标签关联
     *
     * @param articleTag 文章标签关联
     * @return 是否修改成功
     */
    Long updateArticleTagSelective(@Param("articleTag") ArticleTag articleTag);

    /**
     * 全量更新文章标签关联
     *
     * @param articleTag 文章标签关联
     * @return 是否修改成功
     */
    @Update("update article_tag set update_time=#{articleTag.updateTime}," +
            "article_id=#{articleTag.articleId},tag_id=#{articleTag.tagId}," +
            "is_delete=#{articleTag.isDelete} where id=#{articleTag.id} and is_delete=0")
    Long updateArticleTagAll(@Param("articleTag") ArticleTag articleTag);

    /**
     * 获取文章标签关联
     *
     * @param id 文章标签关联id
     * @return 文章标签关联信息
     */
    @Select("select * from article_tag where id=#{id} and is_delete=0 LIMIT 1")
    ArticleTag getArticleTagById(@Param("id") Integer id);

    /**
     * 根据文章id和标签id获取文章标签关联
     *
     * @param articleId 文章id
     * @param tagId     标签id
     * @return 文章标签关联
     */
    @Select("select * from article_tag where article_id=#{articleId} and tag_id=#{tagId} and is_delete=0 LIMIT 1")
    ArticleTag getArticleTagByArticleIdAndTagId(@Param("articleId") Integer articleId, @Param("tagId") Integer tagId);

    /**
     * 根据文章id获取对应的标签列表
     *
     * @param articleId 文章id
     * @param keyword   关键词
     * @return 标签列表
     */
    @Select("select tag.* from article_tag left join tag on article_tag.tag_id = tag.id " +
            "where article_tag.article_id=#{articleId} and article_tag.is_delete=0 and tag.name like CONCAT('%',#{keyword},'%')")
    List<Tag> getTagListByArticleId(@Param("articleId") Integer articleId, @Param("keyword") String keyword);
}

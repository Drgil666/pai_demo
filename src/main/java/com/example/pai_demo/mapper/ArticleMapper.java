package com.example.pai_demo.mapper;

import com.example.pai_demo.model.Article;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author GilbertYoung
 * @date 2026/07/13 13:59
 */
@Mapper
public interface ArticleMapper {
    /**
     * 创建文章
     *
     * @param article 创建的文章
     * @return 是否创建成功
     */
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("insert into article (create_time, update_time, category_id, title, user_id, is_delete,status) " +
            "values (#{article.createTime},#{article.updateTime},#{article.categoryId},#{article.title},#{article.userId},#{article.isDelete},#{article.status})")
    Boolean createArticle(@Param("article") Article article);

    /**
     * 增量更新文章
     *
     * @param article 更新的文章
     * @return 影响的行数
     */
    Long updateArticleSelective(@Param("article") Article article);

    /**
     * 全量更新
     *
     * @param article 更新的文章
     * @return 影响的行数
     */
    @Update("update article set update_time=#{article.updateTime}," +
            "category_id=#{article.categoryId},title=#{article.title}," +
            "user_id=#{article.userId},is_delete=#{article.isDelete}," +
            "status=#{article.status} where id=#{article.id} and is_delete=0")
    Long updateArticleAll(@Param("article") Article article);

    /**
     * 根据id获取文章
     *
     * @param id 文章id
     * @return 文章信息
     */
    @Select("select * from article where id=#{id} and is_delete=0 LIMIT 1")
    Article getArticleById(@Param("id") Integer id);

    /**
     * 根据用户id获取文章列表
     *
     * @param userId  用户id
     * @param keyword 关键词
     * @return 文章列表
     */
    @Select("select * from article where user_id=#{userId} and is_delete=0 " +
            "and title like CONCAT('%',#{keyword},'%')")
    List<Article> getArticleListByUserId(@Param("userId") Integer userId, @Param("keyword") String keyword);

    /**
     * 根据目录id获取文章列表
     *
     * @param categoryId 目录id
     * @param keyword    关键词
     * @return 文章列表
     */
    @Select("select * from article where category_id=#{categoryId} and is_delete=0 " +
            "and title like CONCAT('%',#{keyword},'%')")
    List<Article> getArticleListByCategoryId(@Param("categoryId") Integer categoryId, @Param("keyword") String keyword);
}

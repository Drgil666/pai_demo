package com.example.pai_demo.mapper;

import com.example.pai_demo.model.Article;
import com.example.pai_demo.model.UserFavorite;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author GilbertYoung
 * @date 2026/07/14 11:03
 */
@Mapper
public interface UserFavoriteMapper {
    /**
     * 创建用户收藏
     *
     * @param userFavorite 用户收藏
     * @return 是否创建成功
     */
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("insert into user_favorite (create_time, update_time, article_id, user_id, is_delete) " +
            "values (#{userFavorite.createTime},#{userFavorite.updateTime},#{userFavorite.articleId}," +
            "#{userFavorite.userId},#{userFavorite.isDelete})")
    Boolean createUserFavorite(@Param("userFavorite") UserFavorite userFavorite);

    /**
     * 增量更新用户收藏
     *
     * @param userFavorite 用户收藏
     * @return 是否修改成功
     */
    Long updateUserFavoriteSelective(@Param("userFavorite") UserFavorite userFavorite);

    /**
     * 全量更新用户收藏
     *
     * @param userFavorite 用户收藏
     * @return 是否修改成功
     */
    @Update("update user_favorite set update_time=#{userFavorite.updateTime}," +
            "article_id=#{userFavorite.articleId},user_id=#{userFavorite.userId}," +
            "is_delete=#{userFavorite.isDelete} where id=#{userFavorite.id} and is_delete=0")
    Long updateUserFavoriteAll(@Param("userFavorite") UserFavorite userFavorite);

    /**
     * 获取用户收藏
     *
     * @param id 用户收藏id
     * @return 用户收藏信息
     */
    @Select("select * from user_favorite where id=#{id} and is_delete=0 LIMIT 1")
    UserFavorite getUserFavoriteById(@Param("id") Integer id);

    /**
     * 根据用户id和文章id获取用户收藏
     *
     * @param userId    用户id
     * @param articleId 文章id
     * @return 用户收藏
     */
    @Select("select * from user_favorite where user_id=#{userId} and article_id=#{articleId} and is_delete=0 LIMIT 1")
    UserFavorite getUserFavoriteByUserIdAndArticleId(@Param("userId") Integer userId, @Param("articleId") Integer articleId);

    /**
     * 根据关键词获取用户收藏的文章列表
     *
     * @param keyword 关键词
     * @return 文章列表
     */
    @Select("select article.* from user_favorite left join article on user_favorite.article_id = article.id " +
            "where user_favorite.user_id=#{userId} and user_favorite.is_delete=0 and article.title like CONCAT('%',#{keyword},'%')")
    List<Article> getUserFavoriteArticleListByUserId(@Param("userId") Integer userId, @Param("keyword") String keyword);

    /**
     * 根据文章id获取收藏数
     *
     * @param articleId 文章id
     * @return 收藏数
     */
    @Select("select count(*) from user_favorite where article_id=#{articleId} and is_delete=0")
    Integer getUserFavoriteCountByArticleId(@Param("articleId") Integer articleId);
}

package com.example.pai_demo.service;

import com.example.pai_demo.model.Article;
import com.example.pai_demo.model.UserFavorite;

import java.util.List;

/**
 * @author GilbertYoung
 * @date 2026/07/14 11:03
 */
public interface UserFavoriteService {
    /**
     * 创建用户收藏
     *
     * @param userFavorite 用户收藏
     * @return 是否创建成功
     */
    Boolean createUserFavorite(UserFavorite userFavorite);

    /**
     * 增量更新用户收藏
     *
     * @param userFavorite 用户收藏
     * @return 是否修改成功
     */
    Long updateUserFavoriteSelective(UserFavorite userFavorite);

    /**
     * 全量更新用户收藏
     *
     * @param userFavorite 用户收藏
     * @return 是否修改成功
     */
    Long updateUserFavoriteAll(UserFavorite userFavorite);

    /**
     * 获取用户收藏
     *
     * @param id 用户收藏id
     * @return 用户收藏信息
     */
    UserFavorite getUserFavoriteById(Integer id);

    /**
     * 根据用户id和文章id获取用户收藏
     *
     * @param userId    用户id
     * @param articleId 文章id
     * @return 用户收藏
     */
    UserFavorite getUserFavoriteByUserIdAndArticleId(Integer userId, Integer articleId);

    /**
     * 根据关键词获取用户收藏的文章列表
     *
     * @param keyword 关键词
     * @return 文章列表
     */
    List<Article> getUserFavoriteArticleListByUserId(Integer userId, String keyword);
}

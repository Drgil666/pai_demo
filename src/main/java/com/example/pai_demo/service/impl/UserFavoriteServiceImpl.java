package com.example.pai_demo.service.impl;

import com.example.pai_demo.mapper.UserFavoriteMapper;
import com.example.pai_demo.model.Article;
import com.example.pai_demo.model.UserFavorite;
import com.example.pai_demo.service.UserFavoriteService;
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
public class UserFavoriteServiceImpl implements UserFavoriteService {
    @Resource
    private UserFavoriteMapper userFavoriteMapper;

    /**
     * 创建用户收藏
     *
     * @param userFavorite 用户收藏
     * @return 是否创建成功
     */
    @Override
    public Boolean createUserFavorite(UserFavorite userFavorite) {
        userFavorite.setCreateTime(new Date());
        userFavorite.setUpdateTime(userFavorite.getCreateTime());
        return userFavoriteMapper.createUserFavorite(userFavorite);
    }

    /**
     * 增量更新用户收藏
     *
     * @param userFavorite 用户收藏
     * @return 是否修改成功
     */
    @Override
    public Long updateUserFavoriteSelective(UserFavorite userFavorite) {
        userFavorite.setUpdateTime(new Date());
        return userFavoriteMapper.updateUserFavoriteSelective(userFavorite);
    }

    /**
     * 全量更新用户收藏
     *
     * @param userFavorite 用户收藏
     * @return 是否修改成功
     */
    @Override
    public Long updateUserFavoriteAll(UserFavorite userFavorite) {
        userFavorite.setUpdateTime(new Date());
        return userFavoriteMapper.updateUserFavoriteAll(userFavorite);
    }

    /**
     * 获取用户收藏
     *
     * @param id 用户收藏id
     * @return 用户收藏信息
     */
    @Override
    public UserFavorite getUserFavoriteById(Integer id) {
        return userFavoriteMapper.getUserFavoriteById(id);
    }

    /**
     * 根据用户id和文章id获取用户收藏
     *
     * @param userId    用户id
     * @param articleId 文章id
     * @return 用户收藏
     */
    @Override
    public UserFavorite getUserFavoriteByUserIdAndArticleId(Integer userId, Integer articleId) {
        return userFavoriteMapper.getUserFavoriteByUserIdAndArticleId(userId, articleId);
    }

    /**
     * 根据关键词获取用户收藏的文章列表
     *
     * @param userId
     * @param keyword 关键词
     * @return 文章列表
     */
    @Override
    public List<Article> getUserFavoriteArticleListByUserId(Integer userId, String keyword) {
        return userFavoriteMapper.getUserFavoriteArticleListByUserId(userId, keyword);
    }
}

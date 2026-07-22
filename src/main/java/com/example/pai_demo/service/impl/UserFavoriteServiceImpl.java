package com.example.pai_demo.service.impl;

import com.example.pai_demo.enums.ArticleStatisticEventEnum;
import com.example.pai_demo.enums.UserStatisticEventEnum;
import com.example.pai_demo.mapper.UserFavoriteMapper;
import com.example.pai_demo.model.Article;
import com.example.pai_demo.model.UserFavorite;
import com.example.pai_demo.model.event.ArticleStatisticEvent;
import com.example.pai_demo.model.event.UserStatisticEvent;
import com.example.pai_demo.service.UserFavoriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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
    @Resource
    private ApplicationEventPublisher eventPublisher;

    /**
     * 创建用户收藏
     *
     * @param userFavorite 用户收藏
     * @return 是否创建成功
     */
    @Override
    public Boolean createUserFavorite(UserFavorite userFavorite) {
        userFavorite.setIsDelete(0);
        userFavorite.setCreateTime(new Date());
        userFavorite.setUpdateTime(userFavorite.getCreateTime());
        if (userFavoriteMapper.createUserFavorite(userFavorite)) {
            //用户收藏文章时redis同步缓存，使得文章和用户同步更新统计量
            UserStatisticEvent userStatisticEvent = new UserStatisticEvent();
            userStatisticEvent.setUserId(userFavorite.getUserId());
            userStatisticEvent.setType(UserStatisticEventEnum.USER_FAVORITE);
            eventPublisher.publishEvent(userStatisticEvent);
            ArticleStatisticEvent articleStatisticEvent = new ArticleStatisticEvent();
            articleStatisticEvent.setArticleId(userFavorite.getArticleId());
            articleStatisticEvent.setType(ArticleStatisticEventEnum.ARTICLE_FAVORITE);
            eventPublisher.publishEvent(articleStatisticEvent);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 增量更新用户收藏
     *
     * @param userFavorite 用户收藏
     * @return 是否修改成功
     */
    @Override
    public Long updateUserFavoriteSelective(UserFavorite userFavorite) {
        UserFavorite backup = userFavoriteMapper.getUserFavoriteById(userFavorite.getId());
        userFavorite.setUpdateTime(new Date());
        Long result = userFavoriteMapper.updateUserFavoriteSelective(userFavorite);
        if (result != 0) {
            if (userFavorite.getIsDelete() == 1) {
                //删除文章时,修改对应数据
                UserStatisticEvent userStatisticEvent = new UserStatisticEvent();
                userStatisticEvent.setUserId(backup.getUserId());
                userStatisticEvent.setType(UserStatisticEventEnum.USER_FAVORITE_CANCEL);
                eventPublisher.publishEvent(userStatisticEvent);
                ArticleStatisticEvent articleStatisticEvent = new ArticleStatisticEvent();
                articleStatisticEvent.setArticleId(backup.getArticleId());
                articleStatisticEvent.setType(ArticleStatisticEventEnum.ARTICLE_FAVORITE_CANCEL);
                eventPublisher.publishEvent(articleStatisticEvent);
            }
            return result;
        } else {
            return 0L;
        }
    }

    /**
     * 全量更新用户收藏
     *
     * @param userFavorite 用户收藏
     * @return 是否修改成功
     */
    @Override
    public Long updateUserFavoriteAll(UserFavorite userFavorite) {
        UserFavorite backup = userFavoriteMapper.getUserFavoriteById(userFavorite.getId());
        userFavorite.setUpdateTime(new Date());
        Long result = userFavoriteMapper.updateUserFavoriteAll(userFavorite);
        if (result != 0) {
            if (userFavorite.getIsDelete() == 1) {
                //删除文章时,修改对应数据
                UserStatisticEvent userStatisticEvent = new UserStatisticEvent();
                userStatisticEvent.setUserId(backup.getUserId());
                userStatisticEvent.setType(UserStatisticEventEnum.USER_FAVORITE_CANCEL);
                eventPublisher.publishEvent(userStatisticEvent);
                ArticleStatisticEvent articleStatisticEvent = new ArticleStatisticEvent();
                articleStatisticEvent.setArticleId(backup.getArticleId());
                articleStatisticEvent.setType(ArticleStatisticEventEnum.ARTICLE_FAVORITE_CANCEL);
                eventPublisher.publishEvent(articleStatisticEvent);
            }
            return result;
        } else {
            return 0L;
        }
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

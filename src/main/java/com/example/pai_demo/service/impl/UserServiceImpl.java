package com.example.pai_demo.service.impl;

import com.example.pai_demo.dao.TokenDao;
import com.example.pai_demo.mapper.ArticleMapper;
import com.example.pai_demo.mapper.UserFavoriteMapper;
import com.example.pai_demo.mapper.UserFollowMapper;
import com.example.pai_demo.mapper.UserMapper;
import com.example.pai_demo.model.User;
import com.example.pai_demo.model.event.UserStatisticEvent;
import com.example.pai_demo.model.vo.UserVO;
import com.example.pai_demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.pai_demo.enums.UserStatisticEventEnum.*;

/**
 * @author GilbertYoung
 * @date 2026/07/09 09:35
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private UserFollowMapper userFollowMapper;
    @Resource
    private UserFavoriteMapper userFavoriteMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Resource
    private TokenDao tokenDao;

    /**
     * 创建用户
     *
     * @param user 要注册的用户
     * @return 是否创建成功
     */
    @Override
    public Boolean createUser(User user) {
        String encryptPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptPassword);
        user.setCreateTime(new Date());
        user.setIsDelete(0);
        user.setUpdateTime(user.getCreateTime());
        return userMapper.createUser(user);
    }

    /**
     * 增量更新用户
     *
     * @param user 要更新的用户
     * @return 是否更新成功
     */
    @Override
    public Long updateUserSelective(User user) {
        if (user.getPassword() != null) {
            String encryptPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encryptPassword);
        }
        user.setUpdateTime(new Date());
        return userMapper.updateUserSelective(user);
    }

    /**
     * 全量更新用户
     *
     * @param user 要更新的用户
     * @return 是否更新成功
     */
    @Override
    public Long updateUserAll(User user) {
        String encryptPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptPassword);
        user.setUpdateTime(new Date());
        return userMapper.updateUserAll(user);
    }

    /**
     * 根据id获取用户信息
     *
     * @param id 用户id
     * @return 对应的用户信息
     */
    @Override
    public User getUserById(Integer id) {
        return userMapper.getUserById(id);
    }

    /**
     * 根据用户名查找用户
     *
     * @param username 用户名
     * @return 对应的用户信息
     */
    @Override
    public User getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }

    /**
     * 模糊查找用户列表
     *
     * @param keyword 关键词
     * @return 对应的用户信息
     */
    @Override
    public List<UserVO> getUserListByKeyword(String keyword) {
        List<User> userList = userMapper.getUserListByKeyword(keyword);
        List<UserVO> userVOList = new ArrayList<>();
        for (User user : userList) {
            UserVO userVO = getUserVO(user);
            userVOList.add(userVO);
        }
        return userVOList;
    }

    @NotNull
    private UserVO getUserVO(User user) {
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        Long articleCount = tokenDao.hScore(UserStatisticEvent.USER_STATISTIC_EVENT_PREFIX + user.getId(), USER_ARTICLE.getMsg());
        userVO.setArticleCount(articleCount);
        Long subscribeCount = tokenDao.hScore(UserStatisticEvent.USER_STATISTIC_EVENT_PREFIX + user.getId(), USER_FOLLOW.getMsg());
        userVO.setSubscribeCount(subscribeCount);
        Long followerCount = tokenDao.hScore(UserStatisticEvent.USER_STATISTIC_EVENT_PREFIX + user.getId(), USER_FOLLOWER.getMsg());
        userVO.setFollowerCount(followerCount);
        Long favoriteCount = tokenDao.hScore(UserStatisticEvent.USER_STATISTIC_EVENT_PREFIX + user.getId(), USER_FAVORITE.getMsg());
        userVO.setFavoriteCount(favoriteCount);
        return userVO;
    }
}

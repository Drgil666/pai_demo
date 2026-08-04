package com.example.pai_demo.service.impl;

import com.example.pai_demo.mapper.UserFollowMapper;
import com.example.pai_demo.model.User;
import com.example.pai_demo.model.UserFollow;
import com.example.pai_demo.service.UserFollowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author GilbertYoung
 * @date 2026/07/11 15:35
 */
@Service
@Slf4j
public class UserFollowServiceImpl implements UserFollowService {
    @Resource
    private UserFollowMapper userFollowMapper;

    /**
     * 关注用户
     *
     * @param userFollow 关注用户信息
     * @return 是否创建成功
     */
    @Override
    public Boolean createUserFollow(UserFollow userFollow) {
        userFollow.setIsDelete(0);
        userFollow.setCreateTime(new Date());
        userFollow.setUpdateTime(userFollow.getCreateTime());
        return userFollowMapper.createUserFollow(userFollow);
    }

    /**
     * 增量更新用户关注
     *
     * @param userFollow 要更新的用户关注
     * @return 是否更新成功
     */
    @Override
    public Long updateUserFollowSelective(UserFollow userFollow) {
        userFollow.setUpdateTime(new Date());
        return userFollowMapper.updateUserFollowSelective(userFollow);
    }

    /**
     * 根据userId和followId获取关注用户
     *
     * @param userId   用户id
     * @param followId 关注用户id
     * @return 关注用户
     */
    @Override
    public UserFollow getUserFollowByUserIdAndFollowId(Integer userId, Integer followId) {
        return userFollowMapper.getUserFollowByUserIdAndFollowId(userId, followId);
    }

    /**
     * 根据id获取关注用户信息
     *
     * @param id 关注用户id
     * @return 关注用户信息
     */
    @Override
    public UserFollow getUserFollowById(Integer id) {
        return userFollowMapper.getUserFollowById(id);
    }

    /**
     * 获取用户关注的用户列表
     *
     * @param id      用户id
     * @param keyword 关键词
     * @return 关注用户列表
     */
    @Override
    public List<User> getFollowUserListByUserId(Integer id, String keyword) {
        return userFollowMapper.getFollowUserListByUserId(id, keyword);
    }
}

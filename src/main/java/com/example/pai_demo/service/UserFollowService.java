package com.example.pai_demo.service;

import com.example.pai_demo.model.User;
import com.example.pai_demo.model.UserFollow;

import java.util.List;

/**
 * @author GilbertYoung
 * @date 2026/07/11 15:33
 */
public interface UserFollowService {
    /**
     * 关注用户
     *
     * @param userFollow 关注用户信息
     * @return 是否创建成功
     */
    Boolean createUserFollow(UserFollow userFollow);

    /**
     * 增量更新用户关注
     *
     * @param userFollow 要更新的用户关注
     * @return 是否更新成功
     */
    Long updateUserFollowSelective(UserFollow userFollow);

    /**
     * 根据userId和followId获取关注用户
     *
     * @param userId   用户id
     * @param followId 关注用户id
     * @return 关注用户
     */
    UserFollow getUserFollowByUserIdAndFollowId(Integer userId, Integer followId);

    /**
     * 根据id获取关注用户信息
     *
     * @param id 关注用户id
     * @return 关注用户信息
     */
    UserFollow getUserFollowById(Integer id);

    /**
     * 获取用户关注的用户列表
     *
     * @param id      用户id
     * @param keyword 关键词
     * @return 关注用户列表
     */
    List<User> getFollowUserListByUserId(Integer id, String keyword);
}

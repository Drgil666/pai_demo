package com.example.pai_demo.service;

import com.example.pai_demo.model.vo.UserActivityVO;

import java.util.List;

/**
 * @author GilbertYoung
 * @date 2026/07/09 15:40
 */
public interface TokenService {
    /**
     * 根据用户id生成登录token
     *
     * @param userId 用户id
     * @return 登录token
     */
    String generateToken(Integer userId);

    /**
     * 根据登录token获取用户id
     *
     * @param token 登录token
     * @return 用户id
     */
    Integer getUserIdByToken(String token);

    /**
     * 获取用户当天的活跃度
     *
     * @param userId 用户id
     * @return 用户的活跃度
     */
    Double getUserDailyActivityRank(Integer userId);

    /**
     * 获取用户当月活跃度
     *
     * @param userId 用户id
     * @return 用户的活跃度
     */
    Double getUserMonthlyActivityRank(Integer userId);

    /**
     * 获取每日活跃度前size个最高活跃度的成员
     *
     * @param size 成员数量
     * @return 成员信息
     */
    List<UserActivityVO> getDailyActivityRank(Integer size);

    /**
     * 获取每月活跃度前size个最高活跃度的成员
     *
     * @param size 成员数量
     * @return 成员信息
     */
    List<UserActivityVO> getMonthlyActivityRank(Integer size);
}

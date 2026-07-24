package com.example.pai_demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.pai_demo.dao.TokenDao;
import com.example.pai_demo.exception.ErrorCode;
import com.example.pai_demo.exception.ErrorException;
import com.example.pai_demo.mapper.UserMapper;
import com.example.pai_demo.model.User;
import com.example.pai_demo.model.vo.RedisUserVO;
import com.example.pai_demo.model.vo.UserActivityVO;
import com.example.pai_demo.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

import static com.example.pai_demo.constants.errorDict.GET_TOKEN_ERROR;

/**
 * @author GilbertYoung
 * @date 2026/07/09 15:48
 */
@Service
@Slf4j
public class TokenServiceImpl implements TokenService {
    @Resource
    private TokenDao tokenDao;
    @Resource
    private UserMapper userMapper;

    /**
     * 获取用户当天的活跃度
     *
     * @param userId 用户id
     * @return 用户的活跃度
     */
    @Override
    public Double getUserDailyActivityRank(Integer userId) {
        return tokenDao.zScore(tokenDao.getDailyKey(), userId.toString());

    }

    /**
     * 获取用户当月活跃度
     *
     * @param userId 用户id
     * @return 用户的活跃度
     */
    @Override
    public Double getUserMonthlyActivityRank(Integer userId) {
        return tokenDao.zScore(tokenDao.getMonthlyKey(), userId.toString());
    }

    /**
     * 获取每日活跃度前size个最高活跃度的成员
     *
     * @param size 成员数量
     * @return 成员信息
     */
    @Override
    public List<UserActivityVO> getDailyActivityRank(Integer size) {
        Set<ZSetOperations.TypedTuple<String>> result = tokenDao.getTopRank(tokenDao.getDailyKey(), size);
        List<UserActivityVO> userActivityVOList = new ArrayList<>();
        int index = 1;
        for (ZSetOperations.TypedTuple<String> tuple : result) {
            String userId = tuple.getValue();
            Double score = tuple.getScore();
            User user = userMapper.getUserById(Integer.valueOf(userId));
            UserActivityVO userActivityVO = new UserActivityVO();
            BeanUtils.copyProperties(user, userActivityVO);
            userActivityVO.setRank(index++);
            userActivityVO.setScore(score);
            userActivityVOList.add(userActivityVO);
        }
        return userActivityVOList;
    }

    /**
     * 获取每月活跃度前size个最高活跃度的成员
     *
     * @param size 成员数量
     * @return 成员信息
     */
    @Override
    public List<UserActivityVO> getMonthlyActivityRank(Integer size) {
        Set<ZSetOperations.TypedTuple<String>> result = tokenDao.getTopRank(tokenDao.getDailyKey(), size);
        List<UserActivityVO> userActivityVOList = new ArrayList<>();
        int index = 1;
        for (ZSetOperations.TypedTuple<String> tuple : result) {
            String userId = tuple.getValue();
            Double score = tuple.getScore();
            User user = userMapper.getUserById(Integer.valueOf(userId));
            UserActivityVO userActivityVO = new UserActivityVO();
            BeanUtils.copyProperties(user, userActivityVO);
            userActivityVO.setRank(index++);
            userActivityVO.setScore(score);
            userActivityVOList.add(userActivityVO);
        }
        return userActivityVOList;
    }

    /**
     * 根据用户id生成登录token
     *
     * @param userId 用户id
     * @return 登录token
     */
    @Override
    public String generateToken(Integer userId) {
        String token = UUID.randomUUID().toString();
        while (tokenDao.getValue("login:token:" + token) != null) {
            token = UUID.randomUUID().toString();
        }
        RedisUserVO redisUserVO = new RedisUserVO();
        redisUserVO.setUserId(userId);
        redisUserVO.setLoginTime(new Date().getTime());
        tokenDao.setValue("login:token:" + token, JSON.toJSONString(redisUserVO), true);
        tokenDao.setValue("login:user:" + userId, token, true);
        return token;
    }

    /**
     * 根据登录token获取用户id
     *
     * @param token 登录token
     * @return 用户id
     */
    @Override
    public Integer getUserIdByToken(String token) {
        String value1 = tokenDao.getValue("login:token:" + token);
        RedisUserVO redisUserVO = JSONObject.toJavaObject(JSONObject.parseObject(value1), RedisUserVO.class);
        if (redisUserVO == null) {
            throw new ErrorException(ErrorCode.TOKEN_AUTHORIZE_ILLEGAL, GET_TOKEN_ERROR);
        }
        Integer userId = redisUserVO.getUserId();
        String value2 = tokenDao.getValue("login:user:" + userId);
        if (value2.equals(token)) {
            return userId;
        } else {
            throw new ErrorException(ErrorCode.TOKEN_AUTHORIZE_ILLEGAL, GET_TOKEN_ERROR);
        }
    }
}

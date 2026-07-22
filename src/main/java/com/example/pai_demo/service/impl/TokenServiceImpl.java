package com.example.pai_demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.pai_demo.dao.TokenDao;
import com.example.pai_demo.exception.ErrorCode;
import com.example.pai_demo.exception.ErrorException;
import com.example.pai_demo.model.vo.RedisUserVO;
import com.example.pai_demo.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

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

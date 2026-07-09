package com.example.pai_demo.service;

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
     * @param token 登录token
     * @return 用户id
     */
    Integer getUserIdByToken(String token);
}

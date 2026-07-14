package com.example.pai_demo.service.impl;

import com.example.pai_demo.mapper.UserMapper;
import com.example.pai_demo.model.User;
import com.example.pai_demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author GilbertYoung
 * @date 2026/07/09 09:35
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

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
    public List<User> getUserListByKeyword(String keyword) {
        return userMapper.getUserListByKeyword(keyword);
    }
}

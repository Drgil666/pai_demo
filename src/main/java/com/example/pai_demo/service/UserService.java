package com.example.pai_demo.service;

import com.example.pai_demo.model.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author GilbertYoung
 * @date 2026/07/09 09:33
 */
public interface UserService {
    /**
     * 创建用户
     *
     * @param user 要注册的用户
     * @return 是否创建成功
     */
    Boolean createUser(User user);

    /**
     * 增量更新用户
     *
     * @param user 要更新的用户
     * @return 是否更新成功
     */
    Long updateUserSelective(User user);

    /**
     * 全量更新用户
     *
     * @param user 要更新的用户
     * @return 是否更新成功
     */
    Long updateUserAll(User user);

    /**
     * 根据id获取用户信息
     *
     * @param id 用户id
     * @return 对应的用户信息
     */
    User getUserById(Integer id);
    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 对应的用户信息
     */
    User getUserByUsername(String username);

    /**
     * 模糊查找用户列表
     *
     * @param keyword 关键词
     * @return 对应的用户信息
     */
    List<User> getUserListByKeyword(String keyword);
}

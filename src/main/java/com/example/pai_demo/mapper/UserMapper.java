package com.example.pai_demo.mapper;

import com.example.pai_demo.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author GilbertYoung
 * @date 2026/07/09 08:07
 */
@Mapper
public interface UserMapper {
    /**
     * 创建用户
     *
     * @param user 要注册的用户
     * @return 是否创建成功
     */
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("insert into user (username,password,avatar,privilege,nick,create_time,update_time) values " +
            "(#{user.username},#{user.password},#{user.avatar},#{user.privilege},#{user.nick},#{user.createTime},#{user.updateTime})")
    Boolean createUser(@Param("user") User user);

    /**
     * 增量更新用户
     *
     * @param user 要更新的用户
     * @return 是否更新成功
     */
    Long updateUserSelective(@Param("user") User user);

    /**
     * 全量更新用户
     *
     * @param user 要更新的用户
     * @return 是否更新成功
     */
    @Update("update user set password=#{user.password},avatar=#{user.avatar}," +
            "privilege=#{user.privilege},nick=#{user.nick},update_time=#{user.updateTime} where id=#{user.id} and is_delete=0")
    Long updateUserAll(@Param("user") User user);

    /**
     * 根据id获取用户信息
     *
     * @param id 用户id
     * @return 对应的用户信息
     */
    @Select("select * from user where id=#{id} and is_delete=0 LIMIT 1")
    User getUserById(@Param("id") Integer id);

    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 对应的用户信息
     */
    @Select("select * from user where username=#{username} LIMIT 1")
    User getUserByUsername(@Param("username") String username);

    /**
     * 模糊查找用户列表
     *
     * @param keyword 关键词
     * @return 对应的用户信息
     */
    @Select("select * from user where is_delete=0 and nick like CONCAT('%',#{keyword},'%')")
    List<User> getUserListByKeyword(@Param("keyword") String keyword);
}

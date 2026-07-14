package com.example.pai_demo.mapper;

import com.example.pai_demo.model.User;
import com.example.pai_demo.model.UserFollow;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author GilbertYoung
 * @date 2026/07/09 09:27
 */
@Mapper
public interface UserFollowMapper {
    /**
     * 关注用户
     *
     * @param userFollow 关注用户信息
     * @return 是否创建成功
     */
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("insert into user_follow (create_time, update_time, follow_id, user_id, is_delete) values " +
            "(#{userFollow.createTime},#{userFollow.updateTime},#{userFollow.followId},#{userFollow.userId},#{userFollow.isDelete})")
    Boolean createUserFollow(@Param("userFollow") UserFollow userFollow);

    /**
     * 增量更新用户关注
     *
     * @param userFollow 要更新的用户关注
     * @return 是否更新成功
     */
    Long updateUserFollowSelective(@Param("userFollow") UserFollow userFollow);

    /**
     * 根据userId和followId获取关注用户
     *
     * @param userId   用户id
     * @param followId 关注用户id
     * @return 关注用户
     */
    @Select("select * from user_follow where user_id=#{userId} and is_delete=0 and follow_id=#{followId} LIMIT 1")
    UserFollow getUserFollowByUserIdAndFollowId(@Param("userId") Integer userId,
                                                @Param("followId") Integer followId);

    /**
     * 根据id获取关注用户信息
     *
     * @param id 关注用户id
     * @return 关注用户信息
     */
    @Select("select * from user_follow where id=#{id} and is_delete=0 LIMIT 1")
    UserFollow getUserFollowById(@Param("id") Integer id);

    /**
     * 获取用户关注的用户列表
     *
     * @param id      用户id
     * @param keyword 关键词
     * @return 关注用户列表
     */
    @Select("select `user`.* from user_follow left join `user` on user_follow.follow_id=`user`.id " +
            "where user_follow.user_id=#{id} and user_follow.is_delete=0 and user.nick like CONCAT('%',#{keyword},'%')")
    List<User> getFollowUserListByUserId(@Param("id") Integer id, @Param("keyword") String keyword);
}

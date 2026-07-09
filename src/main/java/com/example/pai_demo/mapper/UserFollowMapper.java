package com.example.pai_demo.mapper;

import com.example.pai_demo.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author GilbertYoung
 * @date 2026/07/09 09:27
 */
@Mapper
public interface UserFollowMapper {
    @Select("select `user`.* from user_follow left join `user` on user_follow.follow_id=`user`.id " +
            "where user_follow.user_id=#{id}")
    List<User> getFollowUserListByUserId(@Param("id") Integer id);
}

package com.example.pai_demo.mapper;

import com.example.pai_demo.model.UserHistory;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author GilbertYoung
 * @date 2026/07/11 16:38
 */
@Mapper
public interface UserHistoryMapper {
    /**
     * 创建用户操作流水
     *
     * @param userHistory 用户操作流水信息
     * @return 是否操作成功
     */
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("insert into user_history (create_time, update_time, object_id, user_id, is_comment, is_favorite, is_like, is_read, is_subscribe, is_delete) values " +
            "(#{userHistory.createTime},#{userHistory.updateTime},#{userHistory.objectId},#{userHistory.userId}," +
            "#{userHistory.isComment},#{userHistory.isFavorite},#{userHistory.isLike},#{userHistory.isRead},#{userHistory.isSubscribe},#{userHistory.isDelete})")
    Boolean createUserHistory(@Param("userHistory") UserHistory userHistory);

    /**
     * 获取用户流水操作列表
     *
     * @param userId 用户id
     * @return 用户流水操作列表
     */
    @Select("select * from user_history where user_id=#{userId}")
    List<UserHistory> getUserHistoryList(@Param("userId") Integer userId);
}

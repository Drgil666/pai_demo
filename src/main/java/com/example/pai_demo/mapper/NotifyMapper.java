package com.example.pai_demo.mapper;

import com.example.pai_demo.model.Notify;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author GilbertYoung
 * @date 2026/07/22 08:07
 */
@Mapper
public interface NotifyMapper {
    /**
     * 创建通知
     *
     * @param notify 通知
     * @return 是否创建成功
     */
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("insert into notify (create_time, update_time, operate_user_id, notify_user_id, content, type, is_read, is_delete) " +
            "values (#{notify.createTime},#{notify.updateTime},#{notify.operateUserId},#{notify.notifyUserId}," +
            "#{notify.content},#{notify.type},#{notify.isRead},#{notify.isDelete})")
    Boolean createNotify(@Param("notify") Notify notify);

    /**
     * 增量更新通知
     *
     * @param notify 通知
     * @return 影响的行数
     */
    Long updateNotifySelective(@Param("notify") Notify notify);

    /**
     * 全量更新通知
     *
     * @param notify 通知
     * @return 影响的行数
     */
    @Update("update notify set update_time=#{notify.updateTime}," +
            "operate_user_id=#{notify.operateUserId},notify_user_id=#{notify.notifyUserId}," +
            "content=#{notify.content},type=#{notify.type},is_read=#{notify.isRead}," +
            "is_delete=#{notify.isDelete} where id=#{notify.id} and is_delete=0")
    Long updateNotifyAll(@Param("notify") Notify notify);

    /**
     * 根据id获取通知
     *
     * @param id 通知id
     * @return 通知信息
     */
    @Select("select * from notify where id=#{id} and is_delete=0 LIMIT 1")
    Notify getNotifyById(@Param("id") Integer id);

    /**
     * 根据用户id获取通知列表
     *
     * @param userId 被通知的用户id
     * @return 通知列表
     */
    @Select("select * from notify where notify_user_id=#{userId} and is_delete=0")
    List<Notify> getNotifyListByUserId(@Param("userId") Integer userId);
}

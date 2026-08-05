package com.example.pai_demo.mapper;

import com.example.pai_demo.model.message.NotifyEventMessage;
import org.apache.ibatis.annotations.*;

/**
 * @author GilbertYoung
 * @date 2026/08/06 10:30
 */
@Mapper
public interface NotifyEventMessageMapper {

    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("insert into mq_notify_msg (msg_id, operate_user_id, notify_user_id, content, type, is_read, is_delete, create_time, update_time) " +
            "values (#{msg.msgId}, #{msg.operateUserId}, #{msg.notifyUserId}, #{msg.content}, #{msg.type}, #{msg.isRead}, #{msg.isDelete}, #{msg.createTime}, #{msg.updateTime})")
    Long create(@Param("msg") NotifyEventMessage msg);

    @Select("select * from mq_notify_msg where id = #{id} LIMIT 1")
    NotifyEventMessage getById(@Param("id") Long id);

    @Select("select * from mq_notify_msg where msg_id = #{msgId} LIMIT 1")
    NotifyEventMessage getByMsgId(@Param("msgId") Long msgId);
}

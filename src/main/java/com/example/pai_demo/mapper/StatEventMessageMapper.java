package com.example.pai_demo.mapper;

import com.example.pai_demo.model.message.StatEventMessage;
import org.apache.ibatis.annotations.*;

/**
 * @author GilbertYoung
 * @date 2026/08/05 15:00
 */
@Mapper
public interface StatEventMessageMapper {

    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("insert into mq_event_msg (msg_id, event_type, target_id, create_time, update_time, is_delete) " +
            "values (#{msg.msgId}, #{msg.eventType}, #{msg.targetId}, #{msg.createTime}, #{msg.updateTime}, #{msg.isDelete})")
    Long create(@Param("msg") StatEventMessage msg);

    @Select("select * from mq_event_msg where id = #{id} LIMIT 1")
    StatEventMessage getById(@Param("id") Long id);

    @Select("select * from mq_event_msg where msg_id = #{msgId} LIMIT 1")
    StatEventMessage getByMsgId(@Param("msgId") Long msgId);
}

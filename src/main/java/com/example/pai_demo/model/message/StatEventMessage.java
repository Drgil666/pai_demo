package com.example.pai_demo.model.message;

import com.example.pai_demo.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 统计事件 RocketMQ 消息体，msgId 由雪花算法生成保证幂等。
 *
 * @author GilbertYoung
 * @date 2026/07/25 10:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mq_event_msg")
@ApiModel(value = "mq_event_msg", description = "mq_event_msg")
public class StatEventMessage extends BaseEntity {
    /**
     * 雪花算法消息ID，用于幂等校验
     */
    @ApiModelProperty(value = "雪花算法消息ID，用于幂等校验")
    @Column(name = "msg_id", unique = true, nullable = false)
    private Long msgId;
    /**
     * 事件类型(对应枚举 name，如 USER_ARTICLE)
     */
    @ApiModelProperty(value = "事件类型(对应枚举 name，如 USER_ARTICLE)")
    @Column(name = "event_type", nullable = false)
    private String eventType;
    /**
     * 目标ID(userId / articleId / commentId)
     */
    @ApiModelProperty(value = "目标ID(userId / articleId / commentId)")
    @Column(name = "target_id", nullable = false)
    private Integer targetId;
}

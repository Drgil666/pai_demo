package com.example.pai_demo.model.message;

import com.example.pai_demo.model.Notify;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 通知事件消息体，msgId 由雪花算法生成保证幂等。
 *
 * @author GilbertYoung
 * @date 2026/08/06 10:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mq_notify_msg")
@ApiModel(value = "mq_notify_msg", description = "mq_notify_msg")
public class NotifyEventMessage extends Notify {
    /**
     * 雪花算法消息ID，用于幂等校验
     */
    @ApiModelProperty(value = "雪花算法消息ID，用于幂等校验")
    @Column(name = "msg_id", unique = true, nullable = false)
    private long msgId;

    public NotifyEventMessage(Notify notify) {
        this.msgId = 0L;
        this.setOperateUserId(notify.getOperateUserId());
        this.setNotifyUserId(notify.getNotifyUserId());
        this.setContent(notify.getContent());
        this.setType(notify.getType());
        this.setIsRead(notify.getIsRead());
        this.setIsDelete(notify.getIsDelete());
        this.setCreateTime(notify.getCreateTime());
        this.setUpdateTime(notify.getUpdateTime());
    }

    public Notify getNotify() {
        return this;
    }
}

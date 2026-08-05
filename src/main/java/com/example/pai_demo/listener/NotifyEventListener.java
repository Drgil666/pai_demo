package com.example.pai_demo.listener;

import com.alibaba.fastjson.JSON;
import com.example.pai_demo.model.message.NotifyEventMessage;
import com.example.pai_demo.utils.SnowflakeIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.example.pai_demo.rocketmq.RocketMQTopicConfig.TOPIC_STAT_NOTIFY;

/**
 * 通知监听 → 转发到 RocketMQ NotifyConsumer
 *
 * @author GilbertYoung
 * @date 2026/08/06 10:00
 */
@Component
@Slf4j
public class NotifyEventListener {
    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @EventListener(classes = NotifyEventMessage.class)
    @Async
    public void notifyEventListener(NotifyEventMessage event) {
        event.setMsgId(SnowflakeIdUtil.generateId());
        String payload = JSON.toJSONString(event);
        rocketMQTemplate.convertAndSend(TOPIC_STAT_NOTIFY, payload);
        log.info("Notify event sent: type={}, userId={}, msgId={}",
                event.getNotify().getType(), event.getNotify().getNotifyUserId(), event.getMsgId());
    }
}

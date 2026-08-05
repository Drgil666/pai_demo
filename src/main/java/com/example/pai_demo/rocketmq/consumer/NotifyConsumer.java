package com.example.pai_demo.rocketmq.consumer;

import com.alibaba.fastjson.JSON;
import com.example.pai_demo.mapper.NotifyEventMessageMapper;
import com.example.pai_demo.model.Notify;
import com.example.pai_demo.model.message.NotifyEventMessage;
import com.example.pai_demo.service.NotifyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.example.pai_demo.rocketmq.RocketMQTopicConfig.GROUP_STAT_NOTIFY;
import static com.example.pai_demo.rocketmq.RocketMQTopicConfig.TOPIC_STAT_NOTIFY;

/**
 * 通知消费者
 *
 * @author GilbertYoung
 * @date 2026/08/06 10:00
 */
@Component
@Slf4j
@ConditionalOnProperty(name = "rocketmq.consumer.enabled", havingValue = "true")
@RocketMQMessageListener(topic = TOPIC_STAT_NOTIFY, consumerGroup = GROUP_STAT_NOTIFY)
public class NotifyConsumer implements RocketMQListener<String> {

    @Resource
    private NotifyService notifyService;
    @Resource
    private NotifyEventMessageMapper notifyEventMessageMapper;
    @Resource(name = "statConsumerExecutor")
    private ThreadPoolTaskExecutor executor;

    @Override
    public void onMessage(String message) {
        executor.execute(() -> {
            try {
                NotifyEventMessage msg = JSON.parseObject(message, NotifyEventMessage.class);
                log.info("Received: {}", message);
                if (msg.getMsgId() != 0L && notifyEventMessageMapper.getByMsgId(msg.getMsgId()) != null) {
                    log.info("Duplicate message skipped: msgId={}", msg.getMsgId());
                    return;
                }
                Notify notify = JSON.parseObject(message, Notify.class);
                notifyService.createNotify(notify);
                notifyEventMessageMapper.create(msg);
                log.info("Notify created: type={}, userId={}", notify.getType(), notify.getNotifyUserId());
            } catch (Exception e) {
                log.error("Notify consume error", e);
            }
        });
    }
}

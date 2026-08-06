package com.example.pai_demo.listener;

import com.alibaba.fastjson.JSON;
import com.example.pai_demo.model.event.ActivityRankStatisticEvent;
import com.example.pai_demo.model.message.StatEventMessage;
import com.example.pai_demo.utils.SnowflakeIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.example.pai_demo.rocketmq.RocketMQTopicConfig.TOPIC_STAT_ACTIVITY;

@Component
@Slf4j
public class ActivityRankStatisticEventListener {
    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @EventListener(classes = ActivityRankStatisticEvent.class)
    @Async
    public void activityRankStatisticEventListener(ActivityRankStatisticEvent event) {
        StatEventMessage msg = new StatEventMessage(SnowflakeIdUtil.generateId(),
                event.getType().name(), event.getUserId());
        rocketMQTemplate.convertAndSend(TOPIC_STAT_ACTIVITY, msg);
        log.info("Sent: {}", JSON.toJSONString(msg));
    }
}

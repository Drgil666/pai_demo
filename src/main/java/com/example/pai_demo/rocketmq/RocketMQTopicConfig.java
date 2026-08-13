package com.example.pai_demo.rocketmq;

/**
 * RocketMQ Topic / ConsumerGroup 常量
 *
 * @author GilbertYoung
 * @date 2026/07/25 10:00
 */
public class RocketMQTopicConfig {
    // ========== Topics ==========
    public static final String TOPIC_STAT_MESSAGE = "pai-stat-message";
    public static final String TOPIC_STAT_ACTIVITY = "pai-stat-activity";
    public static final String TOPIC_STAT_NOTIFY = "pai-stat-notify";

    // ========== Consumer Groups ==========
    public static final String GROUP_STAT_MESSAGE = "pai-stat-message-group";
    public static final String GROUP_STAT_ACTIVITY = "pai-stat-activity-group";
    public static final String GROUP_STAT_NOTIFY = "pai-stat-notify-group";

    public static final String[] ALL_TOPICS = {
            TOPIC_STAT_MESSAGE, TOPIC_STAT_ACTIVITY, TOPIC_STAT_NOTIFY
    };

    private RocketMQTopicConfig() {
    }
}

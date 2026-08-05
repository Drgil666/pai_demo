package com.example.pai_demo.rocketmq;

/**
 * RocketMQ Topic / ConsumerGroup 常量
 *
 * @author GilbertYoung
 * @date 2026/07/25 10:00
 */
public class RocketMQTopicConfig {
    // ========== Topics ==========
    public static final String TOPIC_STAT_USER = "pai-stat-user";
    public static final String TOPIC_STAT_ARTICLE = "pai-stat-article";
    public static final String TOPIC_STAT_ACTIVITY = "pai-stat-activity";
    public static final String TOPIC_STAT_COMMENT = "pai-stat-comment";

    // ========== Consumer Groups ==========
    public static final String GROUP_STAT_USER = "pai-stat-user-group";
    public static final String GROUP_STAT_ARTICLE = "pai-stat-article-group";
    public static final String GROUP_STAT_ACTIVITY = "pai-stat-activity-group";
    public static final String GROUP_STAT_COMMENT = "pai-stat-comment-group";

    public static final String[] ALL_TOPICS = {
            TOPIC_STAT_USER, TOPIC_STAT_ARTICLE, TOPIC_STAT_ACTIVITY, TOPIC_STAT_COMMENT
    };

    private RocketMQTopicConfig() {
    }
}

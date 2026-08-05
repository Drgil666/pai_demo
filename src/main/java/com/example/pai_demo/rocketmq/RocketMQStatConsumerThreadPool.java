package com.example.pai_demo.rocketmq;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * RocketMQ 统计消息消费线程池
 *
 * @author GilbertYoung
 * @date 2026/07/25 10:00
 */
@Configuration
public class RocketMQStatConsumerThreadPool {

    @Value("${rocketmq.consumer.core-pool-size}")
    private int corePoolSize;
    @Value("${rocketmq.consumer.max-pool-size}")
    private int maxPoolSize;
    @Value("${rocketmq.consumer.queue-capacity}")
    private int queueCapacity;

    @Bean("statConsumerExecutor")
    public ThreadPoolTaskExecutor statConsumerExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("stat-consume-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}

package com.example.pai_demo.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 雪花算法ID生成器。
 * 结构：1位保留 + 41位时间戳 + 10位机器ID + 12位序列号。
 * 机器ID从 application.properties 的 snowflake.machine-id 读取。
 *
 * @author GilbertYoung
 * @date 2026/07/25 10:00
 */
@Component
public class SnowflakeIdUtil {
    private static final long START_TIMESTAMP = 1750000000000L;
    private static final long MACHINE_ID_BITS = 10L;
    private static final long SEQUENCE_BITS = 12L;
    private static final long MAX_MACHINE_ID = ~(-1L << MACHINE_ID_BITS);
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);
    private static final long MACHINE_SHIFT = SEQUENCE_BITS;
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + MACHINE_ID_BITS;
    private static volatile SnowflakeIdUtil instance;
    @Value("${snowflake.machine-id}")
    private Long machineId;
    private long sequence = 0L;
    private long lastTimestamp = -1L;

    public static SnowflakeIdUtil getInstance() {
        return instance;
    }

    public static long generateId() {
        return getInstance().nextId();
    }

    @PostConstruct
    public void init() {
        if (machineId > MAX_MACHINE_ID || machineId < 0) {
            throw new IllegalArgumentException("snowflake.machine-id must be 0 ~ " + MAX_MACHINE_ID);
        }
        instance = this;
    }

    public synchronized long nextId() {
        long current = System.currentTimeMillis();
        if (current < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate id");
        }
        if (current == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0) {
                current = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }
        lastTimestamp = current;
        return ((current - START_TIMESTAMP) << TIMESTAMP_SHIFT)
                | (machineId << MACHINE_SHIFT)
                | sequence;
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }
}

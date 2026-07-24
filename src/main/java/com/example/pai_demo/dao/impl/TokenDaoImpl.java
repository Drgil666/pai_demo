package com.example.pai_demo.dao.impl;


import com.example.pai_demo.dao.TokenDao;
import com.example.pai_demo.model.event.ActivityRankStatisticEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Gilbert
 * @date 2020/11/30 16:07
 */
@Component("TokenDaoImpl")
public class TokenDaoImpl implements TokenDao {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Value("${redis.expire.time}")
    private Long expireTime;

    public static final String DAILY_KEY = "daily";
    public static final String MONTHLY_KEY = "monthly";
    public static final DateTimeFormatter DAILY_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final DateTimeFormatter MONTHLY_FORMAT = DateTimeFormatter.ofPattern("yyyyMM");

    /**
     * 为redis设置键值对
     *
     * @param key      键
     * @param value    值
     * @param isExpire 是否过期
     */
    @Override
    public void setValue(String key, String value, Boolean isExpire) {
        if (isExpire) {
            stringRedisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.SECONDS);
        } else {
            stringRedisTemplate.opsForValue().set(key, value);
        }
    }

    /**
     * 获取键对应的值
     *
     * @param key 键
     * @return 对应值
     */
    @Override
    public String getValue(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 删除键对应的值
     *
     * @param key 键
     */
    @Override
    public void deleteValue(String key) {
        stringRedisTemplate.delete(key);
    }

    /**
     * 某个key的域增加值
     *
     * @param key   键值
     * @param field 域
     * @param cnt   增加的值
     * @return 是否成功
     */
    @Override
    public void hIncr(String key, String field, Integer cnt) {
        stringRedisTemplate.execute((RedisCallback<Long>) con -> con.hIncrBy(key.getBytes(StandardCharsets.UTF_8), field.getBytes(StandardCharsets.UTF_8), cnt));
    }

    /**
     * 根据key和域获取值
     *
     * @param key   键值
     * @param field 域
     * @return 对应的值
     */
    @Override
    public Long hScore(String key, String field) {
        String res = stringRedisTemplate.execute((RedisCallback<String>) conn -> {
            byte[] bytes = conn.hGet(key.getBytes(StandardCharsets.UTF_8), field.getBytes(StandardCharsets.UTF_8));
            return bytes != null ? new String(bytes, StandardCharsets.UTF_8) : null;
        });
        return res == null ? 0L : Long.parseLong(res);
    }

    /**
     * 为集合setName中的成员member增加cnt
     *
     * @param setName 集合名
     * @param member  成员名
     * @param cnt     增加的分数
     * @return
     */
    @Override
    public void zIncr(String setName, String member, Integer cnt) {
        stringRedisTemplate.opsForZSet().incrementScore(setName, member, cnt);
    }

    /**
     * 获取setName中成员member的分值
     *
     * @param setName 集合名
     * @param member  成员名
     * @return 对应的分数
     */
    @Override
    public Double zScore(String setName, String member) {
        return stringRedisTemplate.opsForZSet().score(setName, member);
    }

    /**
     * 获取setName中前topNum个成员
     *
     * @param key    集合名
     * @param topNum 榜单成员数
     * @return 返回的集合
     */
    @Override
    public Set<ZSetOperations.TypedTuple<String>> getTopRank(String key, int topNum) {
        return stringRedisTemplate.opsForZSet().reverseRangeWithScores(key, 0, topNum - 1);
    }

    /**
     * 获取当日排行榜的key
     *
     * @return 当日排行榜的key
     */
    @Override
    public String getDailyKey() {
        LocalDate now = LocalDate.now();
        return ActivityRankStatisticEvent.ACTIVITY_RANK_STATISTIC_EVENT_PREFIX + ":" + DAILY_KEY + ":" + DAILY_FORMAT.format(now);
    }

    /**
     * 获取当月排行榜的key
     *
     * @return 当月排行榜的key
     */
    @Override
    public String getMonthlyKey() {
        LocalDate now = LocalDate.now();
        return ActivityRankStatisticEvent.ACTIVITY_RANK_STATISTIC_EVENT_PREFIX + ":" + MONTHLY_KEY + ":" + MONTHLY_FORMAT.format(now);
    }
}

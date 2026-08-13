package com.example.pai_demo.dao;

import org.springframework.data.redis.core.ZSetOperations;

import java.util.Set;

/**
 * @author Gilbert
 * @date 2020/11/30 16:05
 */
public interface TokenDao {
    /**
     * 为redis设置键值对
     *
     * @param key      键
     * @param value    值
     * @param expireTime 是否过期时间
     */
    void setValue(String key, String value, Long expireTime);

    /**
     * 获取键对应的值
     *
     * @param key 键
     * @return 对应值
     */
    String getValue(String key);

    /**
     * 删除键对应的值
     *
     * @param key 键
     */
    void deleteValue(String key);

    /**
     * 某个key的域增加值
     *
     * @param key   键值
     * @param field 域
     * @param cnt   增加的值
     * @return 是否成功
     */
    void hIncr(String key, String field, Long cnt);

    /**
     * 根据key和域获取值
     *
     * @param key   键值
     * @param field 域
     * @return 对应的值
     */
    Long hScore(String key, String field);

    /**
     * 为集合setName中的成员member增加cnt
     *
     * @param setName 集合名
     * @param member  成员名
     * @param cnt     增加的分数
     */
    void zIncr(String setName, String member, Long cnt);

    /**
     * 获取setName中成员member的分值
     *
     * @param setName 集合名
     * @param member  成员名
     * @return 对应的分数
     */
    Double zScore(String setName, String member);

    /**
     * 获取setName中前topNum个成员
     *
     * @param key    集合名
     * @param topNum 榜单成员数
     * @return 返回的集合
     */
    Set<ZSetOperations.TypedTuple<String>> getTopRank(String key, int topNum);

    /**
     * 获取当日排行榜的key
     *
     * @return 当日排行榜的key
     */
    String getDailyKey();

    /**
     * 获取当月排行榜的key
     *
     * @return 当月排行榜的key
     */
    String getMonthlyKey();
}

package com.example.pai_demo.dao;

/**
 * @author Gilbert
 * @date 2020/11/30 16:05
 */
public interface TokenDao {
    /**
     * 为redis设置键值对
     *
     * @param key   键
     * @param value 值
     * @param isExpire 是否过期
     */
    void setValue(String key, String value, Boolean isExpire);

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
}

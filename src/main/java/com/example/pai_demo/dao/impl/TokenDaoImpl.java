package com.example.pai_demo.dao.impl;


import com.example.pai_demo.dao.TokenDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
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
    public Long hIncr(String key, String field, Integer cnt) {
        return stringRedisTemplate.execute((RedisCallback<Long>) con -> con.hIncrBy(key.getBytes(StandardCharsets.UTF_8), field.getBytes(StandardCharsets.UTF_8), cnt));
    }

}

package com.example.pai_demo.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author GilbertYoung
 * @date 2026/07/09 17:32
 */
@Data
public class RedisUserVO {
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 登录时间
     */
    private Long loginTime;
}

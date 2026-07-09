package com.example.pai_demo.model.vo;

/**
 * @author GilbertYoung
 * @date 2026/07/09 17:58
 */

import lombok.Data;

/**
 * 登录成功后返回的用户类
 */
@Data
public class LoginUserVO {
    /**
     * 用户权限
     */
    private Integer privilege;
    /**
     * 用户token
     */
    private String token;
}

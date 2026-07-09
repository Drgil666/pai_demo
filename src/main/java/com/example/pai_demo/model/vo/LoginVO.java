package com.example.pai_demo.model.vo;

/**
 * @author GilbertYoung
 * @date 2026/07/09 17:53
 */

import lombok.Data;

/**
 * 登录用VO
 */
@Data
public class LoginVO {
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
}

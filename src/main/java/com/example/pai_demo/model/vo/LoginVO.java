package com.example.pai_demo.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 登录用VO
 */
@Data
public class LoginVO {
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;
}

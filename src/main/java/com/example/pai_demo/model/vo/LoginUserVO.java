package com.example.pai_demo.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 登录成功后返回的用户类
 */
@Data
public class LoginUserVO {
    /**
     * 用户权限
     */
    @ApiModelProperty(value = "用户权限")
    private Integer privilege;
    /**
     * 用户token
     */
    @ApiModelProperty(value = "用户token")
    private String token;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Integer userId;
}

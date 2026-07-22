package com.example.pai_demo.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author GilbertYoung
 * @date 2026/07/09 17:32
 */
@Data
@ApiModel(value = "RedisUserVO", description = "RedisUserVO")
public class RedisUserVO {
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Integer userId;
    /**
     * 登录时间
     */
    @ApiModelProperty(value = "登录时间")
    private Long loginTime;
}

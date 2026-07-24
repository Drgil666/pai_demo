package com.example.pai_demo.model.vo;

import com.example.pai_demo.model.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author GilbertYoung
 * @date 2026/07/22 14:40
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "用户VO", description = "用户信息及统计数据")
public class UserVO extends User {
    /**
     * 发表文章数
     */
    @ApiModelProperty(value = "发表文章数")
    private Long articleCount;
    /**
     * 关注数
     */
    @ApiModelProperty(value = "关注数")
    private Long subscribeCount;
    /**
     * 粉丝数
     */
    @ApiModelProperty(value = "粉丝数")
    private Long followerCount;
    /**
     * 收藏文章数
     */
    @ApiModelProperty(value = "收藏文章数")
    private Long favoriteCount;
}

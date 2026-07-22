package com.example.pai_demo.model.vo;

import com.example.pai_demo.model.User;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author GilbertYoung
 * @date 2026/07/22 14:40
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "UserVO", description = "UserVO")
public class UserVO extends User {
    /**
     * 发表文章数
     */
    private Integer articleCount;
    /**
     * 关注数
     */
    private Integer subscribeCount;
    /**
     * 粉丝数
     */
    private Integer followerCount;
    /**
     * 收藏文章数
     */
    private Integer favoriteCount;
}

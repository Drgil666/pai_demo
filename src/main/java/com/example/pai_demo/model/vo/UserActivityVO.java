package com.example.pai_demo.model.vo;

import com.example.pai_demo.model.User;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author GilbertYoung
 * @date 2026/07/24 10:08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "UserActivityVO", description = "UserActivityVO")
public class UserActivityVO extends User {
    /**
     * 用户排名
     */
    private Integer rank;
    /**
     * 用户活跃度
     */
    private Double score;
}

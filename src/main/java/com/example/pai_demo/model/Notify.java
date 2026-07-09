package com.example.pai_demo.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author GilbertYoung
 * @date 2026/07/07 10:28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "notify")
public class Notify extends BaseEntity {
    /**
     * 触发操作的用户id
     */
    @ApiModelProperty(value = "触发操作的用户id")
    @Column(name = "operate_user_id", nullable = false)
    private Integer operateUserId;
    /**
     * 被通知的用户id
     */
    @ApiModelProperty(value = "被通知的用户id")
    @Column(name = "notify_user_id", nullable = false)
    private Integer notifyUserId;
    /**
     * 通知的消息内容
     */
    @ApiModelProperty(value = "通知的消息内容")
    @Column(name = "content", nullable = false)
    private String content;
    /**
     * 通知的类型(0-系统,1-评论,2-回复,3-点赞,4-收藏,5-关注)
     */
    @ApiModelProperty(value = "通知的类型(0-系统,1-评论,2-回复,3-点赞,4-收藏,5-关注)")
    private Integer type;
}

package com.example.pai_demo.model;

import io.swagger.annotations.ApiModel;
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
@ApiModel(value = "Notify", description = "Notify")
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
     * 是否已读(0-未读,1-已读)
     */
    @ApiModelProperty(value = "是否已读(0-未读,1-已读)")
    private Integer isRead;

    public static final Integer NOTIFY_SYSTEM = 0;
    /**
     * 通知的类型(0-系统,1-评论,2-点赞,3-收藏,4-关注)
     */
    @ApiModelProperty(value = "通知的类型(0-系统,1-评论,2-点赞,3-收藏,4-关注)")
    private Integer type;
    public static final Integer NOTIFY_COMMENT = 1;
    public static final Integer NOTIFY_LIKE = 2;
    public static final Integer NOTIFY_FAVORITE = 3;
    public static final Integer NOTIFY_SUBSCRIBE = 4;
}

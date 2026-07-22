package com.example.pai_demo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author GilbertYoung
 * @date 2026/07/06 15:43
 */

/**
 * 所有实体的基类
 */
@Data
@MappedSuperclass // 映射父类，不单独建表
@ApiModel(value = "BaseEntity")
public class BaseEntity implements Serializable {
    /**
     * 自增长主键
     */
    @ApiModelProperty(value = "自增长主键")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int comment '主键id'")
    private Integer id;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @Column(name = "create_time", columnDefinition = "datetime comment '创建时间'")
    private Date createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @Column(name = "update_time", columnDefinition = "datetime comment '更新时间'")
    private Date updateTime;
    /**
     * 是否已经删除(0-未删除,1-已删除)
     */
    @ApiModelProperty(value = "是否已经删除(0-未删除,1-已删除)")
    @Column(name = "isDelete", nullable = false, columnDefinition = "int default '0' comment '是否已删除'")
    private Integer isDelete;

    public BaseEntity() {
        this.createTime = new Date();
        this.updateTime = this.createTime;
        this.isDelete = 0;
    }
}

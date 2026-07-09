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
 * @date 2026/07/06 15:43
 */

/**
 * 用户类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user")
@ApiModel(value = "用户", description = "用户详细信息")
public class User extends BaseEntity {
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    @Column(name = "password", nullable = false)
    private String password;
    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "用户昵称")
    @Column(name = "nick", nullable = false)
    private String nick;
    /**
     * 权限(0-管理员,1-普通用户)
     */
    @ApiModelProperty(value = "权限(0-管理员,1-普通用户)")
    @Column(name = "privilege", nullable = false, columnDefinition = "int default '0' comment '权限'")
    private Integer privilege;
    /**
     * 用户头像对应的MongoId
     */
    @ApiModelProperty(value = "用户头像对应的MongoId")
    @Column(name = "avatar")
    private String avatar;
}

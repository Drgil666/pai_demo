package com.example.pai_demo.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author GilbertYoung
 * @date 2026/07/06 15:43
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user")
public class User extends BaseEntity {
    /**
     * 用户名
     */
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    /**
     * 密码
     */
    @Column(name = "password", nullable = false)
    private String password;
    /**
     * 权限(0-管理员,1-普通用户)
     */
    @Column(name = "privilege", nullable = false, columnDefinition = "varchar default '0' comment '权限'")
    private Integer privilege;
    /**
     * 用户头像对应的MongoId
     */
    @Column(name = "avatar")
    private String avatar;
}

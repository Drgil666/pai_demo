package com.example.pai_demo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author GilbertYoung
 * @date 2026/07/06 16:58
 */

/**
 * 文章标签
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "tag")
public class Tag extends BaseEntity {
    /**
     * 标签名称
     */
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    /**
     * 标签创建的用户id
     */
    @Column(name = "user_id", nullable = false)
    private Integer userId;
}

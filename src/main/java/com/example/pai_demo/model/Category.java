package com.example.pai_demo.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author GilbertYoung
 * @date 2026/07/06 16:52
 */

/**
 * 文章所属分类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "catagory")
public class Category extends BaseEntity {
    /**
     * 分类名
     */
    @ApiModelProperty(value = "分类名")
    @Column(name = "name", nullable = false)
    private String name;
    /**
     * 分类的创建者
     */
    @ApiModelProperty(value = "分类的创建者")
    @Column(name = "user_id", nullable = false)
    private Integer userId;
}

package com.example.pai_demo.entity;

/**
 * @author GilbertYoung
 * @date 2026/07/06 17:06
 */

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.repository.CountQuery;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 用户-用户中间表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "user_follow")
public class UserFollow extends BaseEntity {
    /**
     * 用户id
     */
    @Column(name = "user_id", nullable = false)
    private Integer userId;
    /**
     * 关注的用户id
     */
    @Column(name = "follow_id", nullable = false)
    private Integer followId;
}

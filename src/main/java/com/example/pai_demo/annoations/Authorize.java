package com.example.pai_demo.annoations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口权限校验注解。
 * 标注在Controller方法上，根据请求头Authorization中的Bearer Token获取用户身份并校验权限。
 * <p>
 * privilege取值：0-管理员，1-普通用户。
 * 默认{0, 1}表示管理员和普通用户均可访问；
 * {0}表示仅管理员可访问。
 *
 * @author GilbertYoung
 * @date 2026/07/22 10:00
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Authorize {
    int ADMIN = 0;
    int USER = 1;

    /**
     * 允许访问的权限列表，默认{ADMIN, USER}
     */
    int[] value() default {ADMIN, USER};
}

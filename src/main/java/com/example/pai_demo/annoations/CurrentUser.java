package com.example.pai_demo.annoations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 获取当前登录用户ID。
 * 标注在Controller方法参数上，自动从请求头Authorization中解析Bearer Token并获取userId。
 * <p>
 * 用法：public ResponseVO<?> method(@CurrentUser Integer userId) { ... }
 *
 * @author GilbertYoung
 * @date 2026/07/23 10:00
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUser {
}

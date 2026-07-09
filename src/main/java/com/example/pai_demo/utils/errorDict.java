package com.example.pai_demo.utils;

/**
 * @author GilbertYoung
 * @date 2026/07/09 09:45
 */

/**
 * 所有报错信息的字典类
 */
public class errorDict {
    //User相关
    public static final String CREATE_USER_ERROR = "创建用户失败！";
    public static final String UPDATE_USER_ERROR = "更新用户失败！";
    public static final String EXIST_USERNAME_ERROR = "用户名已存在！";
    public static final String USER_NOT_EXIST_ERROR = "用户不存在！";
    public static final String GET_USER_LIST_ERROR = "获取用户列表失败！";
    //Token相关
    public static final String GET_TOKEN_ERROR = "Token过期或错误！请重新登录！";
    public static final String EMPTY_USERNAME_OR_PASSWORD_ERROR = "用户名或密码不可为空！";
    public static final String LOGIN_ERROR = "用户名或密码错误！";
    //分页插件相关
    public static final String CURRENT_ERROR = "current参数错误!";
    public static final String PAGESIZE_ERROR = "pageSize参数错误!";
    public static final String SORTER_ERROR = "sorter参数错误！";

}

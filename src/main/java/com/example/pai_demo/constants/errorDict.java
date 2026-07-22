package com.example.pai_demo.constants;

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
    public static final String USERNAME_EXIST_ERROR = "用户名已存在！";
    public static final String USER_NOT_EXIST_ERROR = "用户不存在！";
    public static final String GET_USER_LIST_ERROR = "获取用户列表失败！";
    //Token相关
    public static final String GET_TOKEN_ERROR = "Token过期或错误！请重新登录！";
    public static final String PRIVILEGE_ERROR = "权限不足！";
    public static final String EMPTY_USERNAME_OR_PASSWORD_ERROR = "用户名或密码不可为空！";
    public static final String LOGIN_ERROR = "用户名或密码错误！";
    //分页插件相关
    public static final String CURRENT_ERROR = "current参数错误!";
    public static final String PAGESIZE_ERROR = "pageSize参数错误!";
    public static final String SORTER_ERROR = "sorter参数错误！";
    //UserFollow相关
    public static final String USER_FOLLOW_EXIST_ERROR = "该用户已关注！";
    public static final String USER_FOLLOW_NOT_EXIST_ERROR = "该用户关注不存在！";
    public static final String CREATE_USER_FOLLOW_ERROR = "关注失败！";
    //Article相关
    public static final String CREATE_ARTICLE_ERROR = "创建文章失败！";
    public static final String ARTICLE_NOT_EXIST_ERROR = "文章不存在！";
    //Category相关
    public static final String CREATE_CATEGORY_ERROR = "创建目录失败！";
    public static final String CATEGORY_NOT_EXIST_ERROR = "目录不存在！";
    //Tag相关
    public static final String CREATE_TAG_ERROR = "创建标签失败！";
    public static final String TAG_NAME_EXIST_ERROR = "标签名已存在！";
    public static final String TAG_NAME_NULL_ERROR = "标签名不可为空！";
    public static final String TAG_NOT_EXIST_ERROR = "标签不存在！";
    //Notify相关
    public static final String NOTIFY_NOT_EXIST_ERROR = "通知不存在！";
    //ArticleDetail相关
    public static final String CREATE_ARTICLE_DETAIL_ERROR = "创建文章内容失败！";
    public static final String ARTICLE_DETAIL_NOT_EXIST_ERROR = "文章内容不存在！";
    //Comment相关
    public static final String CREATE_COMMENT_ERROR = "创建评论失败！";
    public static final String COMMENT_NOT_EXIST_ERROR = "评论不存在！";
    //ArticleTag相关
    public static final String CREATE_ARTICLE_TAG_ERROR = "创建文章标签关联失败！";
    public static final String ARTICLE_TAG_EXIST_ERROR = "该文章标签关联已存在！";
    public static final String ARTICLE_TAG_NOT_EXIST_ERROR = "文章标签关联不存在！";
    //UserFavorite相关
    public static final String CREATE_USER_FAVORITE_ERROR = "收藏失败！";
    public static final String USER_FAVORITE_NOT_EXIST_ERROR = "未收藏该文章！";
    public static final String USER_FAVORITE_EXIST_ERROR = "该文章已收藏！";
    //通用相关
    public static final String ID_NULL_ERROR = "id不可为空！";
    public static final String UPDATE_ERROR = "更新失败！";
}

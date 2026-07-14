package com.example.pai_demo.controller;

import com.example.pai_demo.exception.ErrorCode;
import com.example.pai_demo.model.Article;
import com.example.pai_demo.model.User;
import com.example.pai_demo.model.UserFavorite;
import com.example.pai_demo.model.UserHistory;
import com.example.pai_demo.model.vo.Response;
import com.example.pai_demo.model.vo.ReturnPage;
import com.example.pai_demo.service.ArticleService;
import com.example.pai_demo.service.UserFavoriteService;
import com.example.pai_demo.service.UserHistoryService;
import com.example.pai_demo.service.UserService;
import com.example.pai_demo.utils.AssertionUtil;
import com.example.pai_demo.utils.ListPageUtil;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static com.example.pai_demo.utils.errorDict.*;

/**
 * @author GilbertYoung
 * @date 2026/07/14 11:16
 */
@RestController
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("/api/user_favorite")
public class UserFavoriteController {
    @Resource
    private UserFavoriteService userFavoriteService;
    @Resource
    private UserService userService;
    @Resource
    private ArticleService articleService;
    @Resource
    private UserHistoryService userHistoryService;

    @PostMapping()
    @ApiOperation(value = "创建用户收藏", notes = "创建用户收藏")
    public Response<UserFavorite> createUserFavorite(@RequestBody UserFavorite userFavorite) {

        if (userService.getUserById(userFavorite.getUserId()) == null) {
            return Response.createErr(USER_NOT_EXIST_ERROR);
        }
        if (articleService.getArticleById(userFavorite.getArticleId()) == null) {
            return Response.createErr(ARTICLE_NOT_EXIST_ERROR);
        }
        if (userFavoriteService.getUserFavoriteByUserIdAndArticleId(userFavorite.getUserId(), userFavorite.getArticleId()) != null) {
            return Response.createErr(USER_FAVORITE_EXIST_ERROR);
        }
        userFavoriteService.createUserFavorite(userFavorite);
        if (userFavorite.getId() != null) {
            UserHistory userHistory = new UserHistory();
            userHistory.setUserId(userFavorite.getUserId());
            userHistory.setIsFavorite(1);
            userHistory.setObjectId(userFavorite.getArticleId());
            userHistoryService.createUserHistory(userHistory);
            return Response.createSuc(userFavorite);
        } else {
            return Response.createErr(CREATE_USER_FAVORITE_ERROR);
        }
    }

    @PatchMapping("/{id}")
    @ApiOperation(value = "增量更新用户收藏", notes = "增量更新用户收藏")
    public Response<UserFavorite> updateUserFavoriteSelective(@PathVariable(name = "id") Integer id,
                                                              @RequestBody UserFavorite userFavorite) {
        if (userFavoriteService.getUserFavoriteById(id) == null) {
            return Response.createErr(USER_FAVORITE_NOT_EXIST_ERROR);
        }
        userFavorite.setId(id);
        if (userFavoriteService.updateUserFavoriteSelective(userFavorite) == 1) {
            userFavorite = userFavoriteService.getUserFavoriteById(id);
            UserHistory userHistory = new UserHistory();
            userHistory.setUserId(userFavorite.getUserId());
            userHistory.setIsFavorite(2);
            userHistory.setObjectId(userFavorite.getArticleId());
            userHistoryService.createUserHistory(userHistory);
            return Response.createSuc(userFavorite);
        } else {
            return Response.createErr(UPDATE_ERROR);
        }
    }

    @PostMapping("/{id}")
    @ApiOperation(value = "全量更新用户收藏", notes = "全量更新用户收藏")
    public Response<UserFavorite> updateUserFavoriteAll(@PathVariable(name = "id") Integer id,
                                                        @RequestBody UserFavorite userFavorite) {
        if (userFavoriteService.getUserFavoriteById(id) == null) {
            return Response.createErr(USER_FAVORITE_NOT_EXIST_ERROR);
        }
        userFavorite.setId(id);
        if (userFavoriteService.updateUserFavoriteAll(userFavorite) == 1) {
            userFavorite = userFavoriteService.getUserFavoriteById(id);
            UserHistory userHistory = new UserHistory();
            userHistory.setUserId(userFavorite.getUserId());
            userHistory.setIsFavorite(2);
            userHistory.setObjectId(userFavorite.getArticleId());
            userHistoryService.createUserHistory(userHistory);
            return Response.createSuc(userFavorite);
        } else {
            return Response.createErr(UPDATE_ERROR);
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取用户收藏", notes = "根据id获取用户收藏")
    public Response<UserFavorite> getUserFavoriteById(@PathVariable(name = "id") Integer id) {
        UserFavorite userFavorite = userFavoriteService.getUserFavoriteById(id);
        if (userFavorite != null) {
            return Response.createSuc(userFavorite);
        } else {
            return Response.createErr(ARTICLE_NOT_EXIST_ERROR);
        }
    }

    @GetMapping()
    @ApiOperation(value = "根据用户id获取用户收藏文章列表", notes = "根据用户id获取用户收藏文章列表")
    public Response<ReturnPage<Article>> getArticleListByUserId(@RequestParam("userId") Integer userId,
                                                                @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                                                @RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
                                                                @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                                @RequestParam(value = "sorter", required = false, defaultValue = "{\"update_time\":\"descend\"}") String sorter) {
        AssertionUtil.notNull(userId, ErrorCode.BIZ_PARAM_ILLEGAL, USER_NOT_EXIST_ERROR);
        User user = userService.getUserById(userId);
        if (user == null) {
            return Response.createErr(USER_NOT_EXIST_ERROR);
        }
        ListPageUtil.paging(current, pageSize, sorter);
        List<Article> articleList = userFavoriteService.getUserFavoriteArticleListByUserId(userId, keyword);
        PageInfo<Article> pageInfo = new PageInfo<>(articleList);
        ReturnPage<Article> returnPage = ListPageUtil.returnPage(pageInfo);
        return Response.createSuc(returnPage);
    }
}

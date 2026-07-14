package com.example.pai_demo.controller;

import com.example.pai_demo.exception.ErrorCode;
import com.example.pai_demo.model.Article;
import com.example.pai_demo.model.Category;
import com.example.pai_demo.model.User;
import com.example.pai_demo.model.vo.Response;
import com.example.pai_demo.model.vo.ReturnPage;
import com.example.pai_demo.service.ArticleService;
import com.example.pai_demo.service.CategoryService;
import com.example.pai_demo.service.TokenService;
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
 * @date 2026/07/13 20:18
 */
@RestController
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("/api/article")
public class ArticleController {
    @Resource
    private TokenService tokenService;
    @Resource
    private ArticleService articleService;
    @Resource
    private UserService userService;
    @Resource
    private CategoryService categoryService;

    @PostMapping()
    @ApiOperation(value = "创建文章", notes = "创建文章")
    public Response<Article> createArticle(@RequestBody Article article) {
        if (userService.getUserById(article.getUserId()) == null) {
            return Response.createErr(USER_NOT_EXIST_ERROR);
        }
        articleService.createArticle(article);
        if (article.getId() != null) {
            return Response.createSuc(article);
        } else {
            return Response.createErr(CREATE_ARTICLE_ERROR);
        }
    }

    @PatchMapping("/{id}")
    @ApiOperation(value = "增量更新文章", notes = "增量更新文章")
    public Response<Article> updateArticleSelective(@PathVariable(name = "id") Integer id,
                                                    @RequestBody Article article) {
        if (articleService.getArticleById(id) == null) {
            return Response.createErr(ARTICLE_NOT_EXIST_ERROR);
        }
        article.setId(id);
        if (articleService.updateArticleSelective(article) == 1) {
            return Response.createSuc(articleService.getArticleById(id));
        } else {
            return Response.createErr(UPDATE_ERROR);
        }
    }

    @PostMapping("/{id}")
    @ApiOperation(value = "全量更新文章", notes = "全量更新文章")
    public Response<Article> updateArticleAll(@PathVariable(name = "id") Integer id,
                                              @RequestBody Article article) {
        if (articleService.getArticleById(id) == null) {
            return Response.createErr(ARTICLE_NOT_EXIST_ERROR);
        }
        article.setId(id);
        if (articleService.updateArticleAll(article) == 1) {
            return Response.createSuc(articleService.getArticleById(id));
        } else {
            return Response.createErr(UPDATE_ERROR);
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取文章", notes = "根据id获取文章")
    public Response<Article> getArticleById(@PathVariable(name = "id") Integer id) {
        Article article = articleService.getArticleById(id);
        if (article != null) {
            return Response.createSuc(article);
        } else {
            return Response.createErr(ARTICLE_NOT_EXIST_ERROR);
        }
    }

    @GetMapping("/user_id")
    @ApiOperation(value = "根据用户id获取文章列表", notes = "根据用户id获取文章列表")
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
        List<Article> articleList = articleService.getArticleListByUserId(userId, keyword);
        PageInfo<Article> pageInfo = new PageInfo<>(articleList);
        ReturnPage<Article> returnPage = ListPageUtil.returnPage(pageInfo);
        return Response.createSuc(returnPage);
    }

    @GetMapping("/category_id")
    @ApiOperation(value = "根据目录id获取文章列表", notes = "根据目录id获取文章列表")
    public Response<ReturnPage<Article>> getArticleListByCategoryId(@RequestParam("categoryId") Integer categoryId,
                                                                    @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                                                    @RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
                                                                    @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                                    @RequestParam(value = "sorter", required = false, defaultValue = "{\"update_time\":\"descend\"}") String sorter) {
        AssertionUtil.notNull(categoryId, ErrorCode.BIZ_PARAM_ILLEGAL, USER_NOT_EXIST_ERROR);
        Category category = categoryService.getCategoryById(categoryId);
        if (category == null) {
            return Response.createErr(CATEGORY_NOT_EXIST_ERROR);
        }
        ListPageUtil.paging(current, pageSize, sorter);
        List<Article> articleList = articleService.getArticleListByCategoryId(categoryId, keyword);
        PageInfo<Article> pageInfo = new PageInfo<>(articleList);
        ReturnPage<Article> returnPage = ListPageUtil.returnPage(pageInfo);
        return Response.createSuc(returnPage);
    }
}

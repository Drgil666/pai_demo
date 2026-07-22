package com.example.pai_demo.controller;

import com.example.pai_demo.annoations.Authorize;
import com.example.pai_demo.exception.ErrorCode;
import com.example.pai_demo.model.Article;
import com.example.pai_demo.model.Category;
import com.example.pai_demo.model.User;
import com.example.pai_demo.model.vo.ArticleVO;
import com.example.pai_demo.model.vo.ResponseVO;
import com.example.pai_demo.model.vo.ReturnPageVO;
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
    @Authorize(value = Authorize.USER)
    public ResponseVO<Article> createArticle(@RequestBody Article article) {
        if (userService.getUserById(article.getUserId()) == null) {
            return ResponseVO.createErr(USER_NOT_EXIST_ERROR);
        }
        articleService.createArticle(article);
        if (article.getId() != null) {
            return ResponseVO.createSuc(article);
        } else {
            return ResponseVO.createErr(CREATE_ARTICLE_ERROR);
        }
    }

    @PatchMapping("/{id}")
    @ApiOperation(value = "增量更新文章", notes = "增量更新文章")
    @Authorize(value = Authorize.USER)
    public ResponseVO<Article> updateArticleSelective(@PathVariable(name = "id") Integer id,
                                                      @RequestBody Article article) {
        if (articleService.getArticleById(id) == null) {
            return ResponseVO.createErr(ARTICLE_NOT_EXIST_ERROR);
        }
        article.setId(id);
        if (articleService.updateArticleSelective(article) == 1) {
            return ResponseVO.createSuc(articleService.getArticleById(id));
        } else {
            return ResponseVO.createErr(UPDATE_ERROR);
        }
    }

    @PostMapping("/{id}")
    @ApiOperation(value = "全量更新文章", notes = "全量更新文章")
    @Authorize(value = Authorize.USER)
    public ResponseVO<Article> updateArticleAll(@PathVariable(name = "id") Integer id,
                                                @RequestBody Article article) {
        if (articleService.getArticleById(id) == null) {
            return ResponseVO.createErr(ARTICLE_NOT_EXIST_ERROR);
        }
        article.setId(id);
        if (articleService.updateArticleAll(article) == 1) {
            return ResponseVO.createSuc(articleService.getArticleById(id));
        } else {
            return ResponseVO.createErr(UPDATE_ERROR);
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取文章", notes = "根据id获取文章")
    public ResponseVO<ArticleVO> getArticleVOById(@PathVariable(name = "id") Integer id) {
        ArticleVO articleVO = articleService.getArticleVOById(id);
        if (articleVO != null) {
            return ResponseVO.createSuc(articleVO);
        } else {
            return ResponseVO.createErr(ARTICLE_NOT_EXIST_ERROR);
        }
    }

    @GetMapping("/user_id")
    @ApiOperation(value = "根据用户id获取文章列表", notes = "根据用户id获取文章列表")
    public ResponseVO<ReturnPageVO<ArticleVO>> getArticleVOListByUserId(@RequestParam("userId") Integer userId,
                                                                    @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                                                    @RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
                                                                    @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                                    @RequestParam(value = "sorter", required = false, defaultValue = "{\"update_time\":\"descend\"}") String sorter) {
        AssertionUtil.notNull(userId, ErrorCode.BIZ_PARAM_ILLEGAL, USER_NOT_EXIST_ERROR);
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseVO.createErr(USER_NOT_EXIST_ERROR);
        }
        ListPageUtil.paging(current, pageSize, sorter);
        List<ArticleVO> articleList = articleService.getArticleVOListByUserId(userId, keyword);
        PageInfo<ArticleVO> pageInfo = new PageInfo<>(articleList);
        ReturnPageVO<ArticleVO> returnPageVO = ListPageUtil.returnPage(pageInfo);
        return ResponseVO.createSuc(returnPageVO);
    }
    //TODO:把所有获取的Article都改为VO

    @GetMapping("/category_id")
    @ApiOperation(value = "根据目录id获取文章列表", notes = "根据目录id获取文章列表")
    public ResponseVO<ReturnPageVO<ArticleVO>> getArticleVOListByCategoryId(@RequestParam("categoryId") Integer categoryId,
                                                                          @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                                                          @RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
                                                                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                                          @RequestParam(value = "sorter", required = false, defaultValue = "{\"update_time\":\"descend\"}") String sorter) {
        AssertionUtil.notNull(categoryId, ErrorCode.BIZ_PARAM_ILLEGAL, CATEGORY_NOT_EXIST_ERROR);
        Category category = categoryService.getCategoryById(categoryId);
        if (category == null) {
            return ResponseVO.createErr(CATEGORY_NOT_EXIST_ERROR);
        }
        ListPageUtil.paging(current, pageSize, sorter);
        List<ArticleVO> articleList = articleService.getArticleVOListByCategoryId(categoryId, keyword);
        PageInfo<ArticleVO> pageInfo = new PageInfo<>(articleList);
        ReturnPageVO<ArticleVO> returnPageVO = ListPageUtil.returnPage(pageInfo);
        return ResponseVO.createSuc(returnPageVO);
    }
}

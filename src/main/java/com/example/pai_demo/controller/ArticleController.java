package com.example.pai_demo.controller;

import com.example.pai_demo.annoations.Authorize;
import com.example.pai_demo.enums.ActivityRankStatisticEventEnum;
import com.example.pai_demo.enums.ArticleStatisticEventEnum;
import com.example.pai_demo.enums.UserStatisticEventEnum;
import com.example.pai_demo.exception.ErrorCode;
import com.example.pai_demo.model.Article;
import com.example.pai_demo.model.Category;
import com.example.pai_demo.model.User;
import com.example.pai_demo.model.event.ActivityRankStatisticEvent;
import com.example.pai_demo.model.event.ArticleStatisticEvent;
import com.example.pai_demo.model.event.UserStatisticEvent;
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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static com.example.pai_demo.constants.errorDict.*;

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
    @Resource
    private ApplicationEventPublisher eventPublisher;

    @PostMapping()
    @ApiOperation(value = "创建文章", notes = "创建文章")
    @Authorize(value = Authorize.USER)
    public ResponseVO<Article> createArticle(@RequestBody Article article) {
        if (userService.getUserById(article.getUserId()) == null) {
            return ResponseVO.createErr(USER_NOT_EXIST_ERROR);
        }
        articleService.createArticle(article);
        if (article.getId() != null) {
            //创建文章时redis同步缓存
            UserStatisticEvent userStatisticEvent = new UserStatisticEvent();
            userStatisticEvent.setUserId(article.getUserId());
            userStatisticEvent.setType(UserStatisticEventEnum.USER_ARTICLE);
            eventPublisher.publishEvent(userStatisticEvent);
            //更新用户活跃度
            ActivityRankStatisticEvent activityRankStatisticEvent = new ActivityRankStatisticEvent();
            activityRankStatisticEvent.setUserId(article.getUserId());
            activityRankStatisticEvent.setType(ActivityRankStatisticEventEnum.USER_PUBLISH);
            eventPublisher.publishEvent(activityRankStatisticEvent);
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
        Article backup = articleService.getArticleById(article.getId());
        if (articleService.updateArticleSelective(article) == 1) {
            if (article.getIsDelete() == 1) {
                //删除文章时,修改对应数据
                UserStatisticEvent userStatisticEvent = new UserStatisticEvent();
                userStatisticEvent.setUserId(backup.getUserId());
                userStatisticEvent.setType(UserStatisticEventEnum.USER_ARTICLE_CANCEL);
                eventPublisher.publishEvent(userStatisticEvent);
            }
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
        Article backup = articleService.getArticleById(article.getId());
        if (articleService.updateArticleAll(article) == 1) {
            if (article.getIsDelete() == 1) {
                //删除文章时,修改对应数据
                UserStatisticEvent userStatisticEvent = new UserStatisticEvent();
                userStatisticEvent.setUserId(backup.getUserId());
                userStatisticEvent.setType(UserStatisticEventEnum.USER_ARTICLE_CANCEL);
                eventPublisher.publishEvent(userStatisticEvent);
            }
            return ResponseVO.createSuc(articleService.getArticleById(id));
        } else {
            return ResponseVO.createErr(UPDATE_ERROR);
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取文章", notes = "根据id获取文章")
    public ResponseVO<ArticleVO> getArticleVOById(@PathVariable(name = "id") Integer id) {
        ArticleVO articleVO = articleService.getArticleVOById(id);
        //阅读文章时redis同步缓存
        ArticleStatisticEvent articleStatisticEvent = new ArticleStatisticEvent();
        articleStatisticEvent.setArticleId(articleVO.getId());
        articleStatisticEvent.setType(ArticleStatisticEventEnum.ARTICLE_READ);
        eventPublisher.publishEvent(articleStatisticEvent);
        return ResponseVO.createSuc(articleVO);
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
    //TODO:补充一个给文章点赞的接口
    //TODO:点赞要用MQ来实现！
}

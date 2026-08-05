package com.example.pai_demo.controller;

import com.example.pai_demo.annoations.Authorize;
import com.example.pai_demo.enums.ArticleStatisticEventEnum;
import com.example.pai_demo.enums.UserStatisticEventEnum;
import com.example.pai_demo.exception.ErrorCode;
import com.example.pai_demo.model.*;
import com.example.pai_demo.model.event.ArticleStatisticEvent;
import com.example.pai_demo.model.event.UserStatisticEvent;
import com.example.pai_demo.model.message.NotifyEventMessage;
import com.example.pai_demo.model.vo.ResponseVO;
import com.example.pai_demo.model.vo.ReturnPageVO;
import com.example.pai_demo.service.ArticleService;
import com.example.pai_demo.service.UserFavoriteService;
import com.example.pai_demo.service.UserHistoryService;
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

import static com.example.pai_demo.constants.NotifyConstant.NOTIFY_FAVORITE_CONTENT;
import static com.example.pai_demo.constants.errorDict.*;

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
    @Resource
    private ApplicationEventPublisher eventPublisher;

    @PostMapping()
    @ApiOperation(value = "创建用户收藏", notes = "创建用户收藏")
    @Authorize(value = Authorize.USER)
    public ResponseVO<UserFavorite> createUserFavorite(@RequestBody UserFavorite userFavorite) {

        if (userService.getUserById(userFavorite.getUserId()) == null) {
            return ResponseVO.createErr(USER_NOT_EXIST_ERROR);
        }
        if (articleService.getArticleById(userFavorite.getArticleId()) == null) {
            return ResponseVO.createErr(ARTICLE_NOT_EXIST_ERROR);
        }
        if (userFavoriteService.getUserFavoriteByUserIdAndArticleId(userFavorite.getUserId(), userFavorite.getArticleId()) != null) {
            return ResponseVO.createErr(USER_FAVORITE_EXIST_ERROR);
        }
        userFavoriteService.createUserFavorite(userFavorite);
        if (userFavorite.getId() != null) {
            //用户收藏文章时redis同步缓存，使得文章和用户同步更新统计量
            UserStatisticEvent userStatisticEvent = new UserStatisticEvent();
            userStatisticEvent.setUserId(userFavorite.getUserId());
            userStatisticEvent.setType(UserStatisticEventEnum.USER_FAVORITE);
            eventPublisher.publishEvent(userStatisticEvent);
            ArticleStatisticEvent articleStatisticEvent = new ArticleStatisticEvent();
            articleStatisticEvent.setArticleId(userFavorite.getArticleId());
            articleStatisticEvent.setType(ArticleStatisticEventEnum.ARTICLE_FAVORITE);
            eventPublisher.publishEvent(articleStatisticEvent);
            //创建用户操作流水
            UserHistory userHistory = new UserHistory();
            userHistory.setUserId(userFavorite.getUserId());
            userHistory.setIsFavorite(1);
            userHistory.setObjectId(userFavorite.getArticleId());
            userHistoryService.createUserHistory(userHistory);
            //发送通知
            Notify notify = new Notify();
            notify.setType(Notify.NOTIFY_FAVORITE);
            notify.setOperateUserId(userFavorite.getUserId());
            notify.setContent(NOTIFY_FAVORITE_CONTENT);
            notify.setNotifyUserId(articleService.getArticleById(userFavorite.getArticleId()).getUserId());
            eventPublisher.publishEvent(new NotifyEventMessage(notify));
            return ResponseVO.createSuc(userFavorite);
        } else {
            return ResponseVO.createErr(CREATE_USER_FAVORITE_ERROR);
        }
    }

    @PatchMapping("/{id}")
    @ApiOperation(value = "增量更新用户收藏", notes = "增量更新用户收藏")
    @Authorize(value = Authorize.USER)
    public ResponseVO<UserFavorite> updateUserFavoriteSelective(@PathVariable(name = "id") Integer id,
                                                                @RequestBody UserFavorite userFavorite) {
        if (userFavoriteService.getUserFavoriteById(id) == null) {
            return ResponseVO.createErr(USER_FAVORITE_NOT_EXIST_ERROR);
        }
        userFavorite.setId(id);
        UserFavorite backup = userFavoriteService.getUserFavoriteById(id);
        //创建用户操作流水
        UserHistory userHistory = new UserHistory();
        userHistory.setUserId(backup.getUserId());
        userHistory.setIsFavorite(2);
        userHistory.setObjectId(backup.getArticleId());
        if (userFavoriteService.updateUserFavoriteSelective(userFavorite) == 1) {
            if (userFavorite.getIsDelete() == 1) {
                //删除文章时,修改对应数据
                UserStatisticEvent userStatisticEvent = new UserStatisticEvent();
                userStatisticEvent.setUserId(backup.getUserId());
                userStatisticEvent.setType(UserStatisticEventEnum.USER_FAVORITE_CANCEL);
                eventPublisher.publishEvent(userStatisticEvent);
                ArticleStatisticEvent articleStatisticEvent = new ArticleStatisticEvent();
                articleStatisticEvent.setArticleId(backup.getArticleId());
                articleStatisticEvent.setType(ArticleStatisticEventEnum.ARTICLE_FAVORITE_CANCEL);
                eventPublisher.publishEvent(articleStatisticEvent);
            }
            userHistoryService.createUserHistory(userHistory);
            return ResponseVO.createSuc(userFavorite);
        } else {
            return ResponseVO.createErr(UPDATE_ERROR);
        }
    }

    @PostMapping("/{id}")
    @ApiOperation(value = "全量更新用户收藏", notes = "全量更新用户收藏")
    @Authorize(value = Authorize.USER)
    public ResponseVO<UserFavorite> updateUserFavoriteAll(@PathVariable(name = "id") Integer id,
                                                          @RequestBody UserFavorite userFavorite) {
        if (userFavoriteService.getUserFavoriteById(id) == null) {
            return ResponseVO.createErr(USER_FAVORITE_NOT_EXIST_ERROR);
        }
        userFavorite.setId(id);
        UserFavorite backup = userFavoriteService.getUserFavoriteById(id);
        //创建用户操作流水
        UserHistory userHistory = new UserHistory();
        userHistory.setUserId(backup.getUserId());
        userHistory.setIsFavorite(2);
        userHistory.setObjectId(backup.getArticleId());
        if (userFavoriteService.updateUserFavoriteAll(userFavorite) == 1) {
            if (userFavorite.getIsDelete() == 1) {
                //删除文章时,修改对应数据
                UserStatisticEvent userStatisticEvent = new UserStatisticEvent();
                userStatisticEvent.setUserId(backup.getUserId());
                userStatisticEvent.setType(UserStatisticEventEnum.USER_FAVORITE_CANCEL);
                eventPublisher.publishEvent(userStatisticEvent);
                ArticleStatisticEvent articleStatisticEvent = new ArticleStatisticEvent();
                articleStatisticEvent.setArticleId(backup.getArticleId());
                articleStatisticEvent.setType(ArticleStatisticEventEnum.ARTICLE_FAVORITE_CANCEL);
                eventPublisher.publishEvent(articleStatisticEvent);
            }
            userHistoryService.createUserHistory(userHistory);
            return ResponseVO.createSuc(userFavorite);
        } else {
            return ResponseVO.createErr(UPDATE_ERROR);
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取用户收藏", notes = "根据id获取用户收藏")
    @Authorize(value = Authorize.USER)
    public ResponseVO<UserFavorite> getUserFavoriteById(@PathVariable(name = "id") Integer id) {
        UserFavorite userFavorite = userFavoriteService.getUserFavoriteById(id);
        if (userFavorite != null) {
            return ResponseVO.createSuc(userFavorite);
        } else {
            return ResponseVO.createErr(ARTICLE_NOT_EXIST_ERROR);
        }
    }

    @GetMapping()
    @ApiOperation(value = "根据用户id获取用户收藏文章列表", notes = "根据用户id获取用户收藏文章列表")
    @Authorize(value = Authorize.USER)
    public ResponseVO<ReturnPageVO<Article>> getArticleListByUserId(@RequestParam("userId") Integer userId,
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
        List<Article> articleList = userFavoriteService.getUserFavoriteArticleListByUserId(userId, keyword);
        PageInfo<Article> pageInfo = new PageInfo<>(articleList);
        ReturnPageVO<Article> returnPageVO = ListPageUtil.returnPage(pageInfo);
        return ResponseVO.createSuc(returnPageVO);
    }
}

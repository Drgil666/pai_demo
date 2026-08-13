package com.example.pai_demo.controller;

import com.example.pai_demo.annoations.Authorize;
import com.example.pai_demo.annoations.CurrentUser;
import com.example.pai_demo.enums.ActivityRankStatisticEventEnum;
import com.example.pai_demo.enums.ArticleStatisticEventEnum;
import com.example.pai_demo.exception.ErrorCode;
import com.example.pai_demo.model.Comment;
import com.example.pai_demo.model.Notify;
import com.example.pai_demo.model.UserHistory;
import com.example.pai_demo.model.event.ActivityRankStatisticEvent;
import com.example.pai_demo.model.event.ArticleStatisticEvent;
import com.example.pai_demo.model.message.NotifyEventMessage;
import com.example.pai_demo.model.vo.CommentVO;
import com.example.pai_demo.model.vo.ResponseVO;
import com.example.pai_demo.model.vo.ReturnPageVO;
import com.example.pai_demo.service.ArticleService;
import com.example.pai_demo.service.CommentService;
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

import static com.example.pai_demo.constants.NotifyConstant.NOTIFY_ARTICLE_CONTENT;
import static com.example.pai_demo.constants.NotifyConstant.NOTIFY_COMMENT_CONTENT;
import static com.example.pai_demo.constants.errorDict.*;

/**
 * @author GilbertYoung
 * @date 2026/07/22 14:00
 */
@RestController
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("/api/comment")
public class CommentController {
    @Resource
    private CommentService commentService;
    @Resource
    private ArticleService articleService;
    @Resource
    private UserService userService;
    @Resource
    private UserHistoryService userHistoryService;
    @Resource
    private ApplicationEventPublisher eventPublisher;
    @PostMapping()
    @ApiOperation(value = "创建评论", notes = "创建评论")
    @Authorize(Authorize.USER)
    public ResponseVO<Comment> createComment(@RequestBody Comment comment) {
        if (articleService.getArticleById(comment.getArticleId()) == null) {
            return ResponseVO.createErr(ARTICLE_NOT_EXIST_ERROR);
        }
        if (userService.getUserById(comment.getUserId()) == null) {
            return ResponseVO.createErr(USER_NOT_EXIST_ERROR);
        }
        if (comment.getTopCommentId() != null && commentService.getCommentById(comment.getTopCommentId()) == null) {
            return ResponseVO.createErr(COMMENT_NOT_EXIST_ERROR);
        }
        if (comment.getParentCommentId() != null && commentService.getCommentById(comment.getParentCommentId()) == null) {
            return ResponseVO.createErr(COMMENT_NOT_EXIST_ERROR);
        }
        commentService.createComment(comment);
        if (comment.getId() != null) {
            //创建评论时，同步更新缓存
            ArticleStatisticEvent articleStatisticEvent = new ArticleStatisticEvent();
            articleStatisticEvent.setArticleId(comment.getArticleId());
            articleStatisticEvent.setType(ArticleStatisticEventEnum.ARTICLE_COMMENT);
            eventPublisher.publishEvent(articleStatisticEvent);
            //更新用户活跃度
            ActivityRankStatisticEvent activityRankStatisticEvent = new ActivityRankStatisticEvent();
            activityRankStatisticEvent.setUserId(comment.getUserId());
            activityRankStatisticEvent.setType(ActivityRankStatisticEventEnum.USER_COMMENT);
            eventPublisher.publishEvent(activityRankStatisticEvent);
            //创建通知
            Notify notify = new Notify();
            notify.setType(Notify.NOTIFY_COMMENT);
            notify.setOperateUserId(comment.getUserId());
            if (comment.getTopCommentId() == null) {
                //对文章评论
                notify.setContent(NOTIFY_ARTICLE_CONTENT);
                notify.setNotifyUserId(articleService.getArticleById(comment.getArticleId()).getUserId());
            } else {
                //对评论回复
                notify.setContent(NOTIFY_COMMENT_CONTENT);
                notify.setNotifyUserId(commentService.getCommentById(comment.getParentCommentId()).getUserId());
            }
            eventPublisher.publishEvent(new NotifyEventMessage(notify));
            //创建用户流水
            UserHistory userHistory = new UserHistory();
            userHistory.setUserId(comment.getUserId());
            userHistory.setObjectId(comment.getId());
            userHistory.setIsComment(1);
            userHistoryService.createUserHistory(userHistory);
            return ResponseVO.createSuc(comment);
        } else {
            return ResponseVO.createErr(CREATE_COMMENT_ERROR);
        }
    }

    @PatchMapping("/{id}")
    @ApiOperation(value = "增量更新评论", notes = "增量更新评论")
    @Authorize(Authorize.USER)
    public ResponseVO<Comment> updateCommentSelective(@CurrentUser Integer userId,
                                                      @PathVariable(name = "id") Integer id,
                                                      @RequestBody Comment comment) {
        if (commentService.getCommentById(id) == null) {
            return ResponseVO.createErr(COMMENT_NOT_EXIST_ERROR);
        }
        comment.setId(id);
        Comment backup = commentService.getCommentById(id);
        if (commentService.updateCommentSelective(comment) == 1) {
            if (comment.getIsDelete() == 1) {
                Long cnt = commentService.deleteCommentsByTopCommentId(comment.getId());
                ArticleStatisticEvent articleStatisticEvent = new ArticleStatisticEvent();
                articleStatisticEvent.setArticleId(backup.getArticleId());
                articleStatisticEvent.setType(ArticleStatisticEventEnum.ARTICLE_COMMENT_CANCEL);
                articleStatisticEvent.setCount(cnt + 1);
                eventPublisher.publishEvent(articleStatisticEvent);
                //创建用户流水
                UserHistory userHistory = new UserHistory();
                userHistory.setUserId(backup.getUserId());
                userHistory.setObjectId(backup.getId());
                userHistory.setIsComment(2);
                userHistoryService.createUserHistory(userHistory);
            }
            Comment result = commentService.getCommentById(id);
            return ResponseVO.createSuc(result);
        } else {
            return ResponseVO.createErr(UPDATE_ERROR);
        }
    }

    @PostMapping("/{id}")
    @ApiOperation(value = "全量更新评论", notes = "全量更新评论")
    @Authorize(Authorize.USER)
    public ResponseVO<Comment> updateCommentAll(@PathVariable(name = "id") Integer id,
                                                @RequestBody Comment comment) {
        if (commentService.getCommentById(id) == null) {
            return ResponseVO.createErr(COMMENT_NOT_EXIST_ERROR);
        }
        Comment backup = commentService.getCommentById(id);
        if (commentService.updateCommentAll(comment) == 1) {
            if (comment.getIsDelete() == 1) {
                Long cnt = commentService.deleteCommentsByTopCommentId(comment.getId());
                ArticleStatisticEvent articleStatisticEvent = new ArticleStatisticEvent();
                articleStatisticEvent.setArticleId(backup.getArticleId());
                articleStatisticEvent.setType(ArticleStatisticEventEnum.ARTICLE_COMMENT_CANCEL);
                articleStatisticEvent.setCount(cnt + 1);
                eventPublisher.publishEvent(articleStatisticEvent);
                //创建用户流水
                UserHistory userHistory = new UserHistory();
                userHistory.setUserId(backup.getUserId());
                userHistory.setObjectId(backup.getId());
                userHistory.setIsComment(2);
                userHistoryService.createUserHistory(userHistory);
            }
            Comment result = commentService.getCommentById(id);
            return ResponseVO.createSuc(result);
        } else {
            return ResponseVO.createErr(UPDATE_ERROR);
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取评论", notes = "根据id获取评论")
    @Authorize(Authorize.USER)
    public ResponseVO<Comment> getCommentVOById(@PathVariable(name = "id") Integer id) {
        Comment comment = commentService.getCommentVOById(id);
        if (comment != null) {
            return ResponseVO.createSuc(comment);
        } else {
            return ResponseVO.createErr(COMMENT_NOT_EXIST_ERROR);
        }
    }

    @GetMapping("/article_id")
    @ApiOperation(value = "根据文章id获取评论列表", notes = "根据文章id获取评论列表")
    @Authorize(Authorize.USER)
    public ResponseVO<ReturnPageVO<CommentVO>> getCommentListByArticleId(@RequestParam("articleId") Integer articleId,
                                                                         @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                                                         @RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
                                                                         @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                                         @RequestParam(value = "sorter", required = false, defaultValue = "{\"update_time\":\"descend\"}") String sorter) {
        AssertionUtil.notNull(articleId, ErrorCode.BIZ_PARAM_ILLEGAL, ARTICLE_NOT_EXIST_ERROR);
        if (articleService.getArticleById(articleId) == null) {
            return ResponseVO.createErr(ARTICLE_NOT_EXIST_ERROR);
        }
        ListPageUtil.paging(current, pageSize, sorter);
        List<CommentVO> commentList = commentService.getCommentListByArticleId(articleId, keyword);
        PageInfo<CommentVO> pageInfo = new PageInfo<>(commentList);
        ReturnPageVO<CommentVO> returnPageVO = ListPageUtil.returnPage(pageInfo);
        return ResponseVO.createSuc(returnPageVO);
    }

    @GetMapping("/top_comment_id")
    @ApiOperation(value = "根据顶级评论id获取子评论列表", notes = "根据顶级评论id获取子评论列表")
    @Authorize(Authorize.USER)
    public ResponseVO<ReturnPageVO<CommentVO>> getCommentListByTopCommentId(@RequestParam("topCommentId") Integer topCommentId,
                                                                            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                                                            @RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
                                                                            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                                            @RequestParam(value = "sorter", required = false, defaultValue = "{\"update_time\":\"descend\"}") String sorter) {
        AssertionUtil.notNull(topCommentId, ErrorCode.BIZ_PARAM_ILLEGAL, COMMENT_NOT_EXIST_ERROR);
        if (commentService.getCommentById(topCommentId) == null) {
            return ResponseVO.createErr(COMMENT_NOT_EXIST_ERROR);
        }
        ListPageUtil.paging(current, pageSize, sorter);
        List<CommentVO> commentList = commentService.getCommentListByTopCommentId(topCommentId, keyword);
        PageInfo<CommentVO> pageInfo = new PageInfo<>(commentList);
        ReturnPageVO<CommentVO> returnPageVO = ListPageUtil.returnPage(pageInfo);
        return ResponseVO.createSuc(returnPageVO);
    }
    //TODO:补充一个给评论点赞的接口
}

package com.example.pai_demo.controller;

import com.example.pai_demo.annoations.Authorize;
import com.example.pai_demo.exception.ErrorCode;
import com.example.pai_demo.model.Comment;
import com.example.pai_demo.model.vo.ResponseVO;
import com.example.pai_demo.model.vo.ReturnPageVO;
import com.example.pai_demo.service.ArticleService;
import com.example.pai_demo.service.CommentService;
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
            return ResponseVO.createSuc(comment);
        } else {
            return ResponseVO.createErr(CREATE_COMMENT_ERROR);
        }
    }

    @PatchMapping("/{id}")
    @ApiOperation(value = "增量更新评论", notes = "增量更新评论")
    @Authorize(Authorize.USER)
    public ResponseVO<Comment> updateCommentSelective(@PathVariable(name = "id") Integer id,
                                                      @RequestBody Comment comment) {
        if (commentService.getCommentById(id) == null) {
            return ResponseVO.createErr(COMMENT_NOT_EXIST_ERROR);
        }
        comment.setId(id);
        if (commentService.updateCommentSelective(comment) == 1) {
            Comment result = commentService.getCommentById(id);
            if (comment.getIsDelete() == 1) {
                commentService.deleteCommentsByTopCommentId(result.getTopCommentId());
            }
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
        comment.setId(id);
        if (commentService.updateCommentAll(comment) == 1) {
            Comment result = commentService.getCommentById(id);
            if (comment.getIsDelete() == 1) {
                commentService.deleteCommentsByTopCommentId(result.getTopCommentId());
            }
            return ResponseVO.createSuc(result);
        } else {
            return ResponseVO.createErr(UPDATE_ERROR);
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取评论", notes = "根据id获取评论")
    @Authorize(Authorize.USER)
    public ResponseVO<Comment> getCommentById(@PathVariable(name = "id") Integer id) {
        Comment comment = commentService.getCommentById(id);
        if (comment != null) {
            return ResponseVO.createSuc(comment);
        } else {
            return ResponseVO.createErr(COMMENT_NOT_EXIST_ERROR);
        }
    }

    @GetMapping("/article_id")
    @ApiOperation(value = "根据文章id获取评论列表", notes = "根据文章id获取评论列表")
    @Authorize(Authorize.USER)
    public ResponseVO<ReturnPageVO<Comment>> getCommentListByArticleId(@RequestParam("articleId") Integer articleId,
                                                                       @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                                                       @RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
                                                                       @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                                       @RequestParam(value = "sorter", required = false, defaultValue = "{\"update_time\":\"descend\"}") String sorter) {
        AssertionUtil.notNull(articleId, ErrorCode.BIZ_PARAM_ILLEGAL, ARTICLE_NOT_EXIST_ERROR);
        if (articleService.getArticleById(articleId) == null) {
            return ResponseVO.createErr(ARTICLE_NOT_EXIST_ERROR);
        }
        ListPageUtil.paging(current, pageSize, sorter);
        List<Comment> commentList = commentService.getCommentListByArticleId(articleId, keyword);
        PageInfo<Comment> pageInfo = new PageInfo<>(commentList);
        ReturnPageVO<Comment> returnPageVO = ListPageUtil.returnPage(pageInfo);
        return ResponseVO.createSuc(returnPageVO);
    }

    @GetMapping("/top_comment_id")
    @ApiOperation(value = "根据顶级评论id获取子评论列表", notes = "根据顶级评论id获取子评论列表")
    @Authorize(Authorize.USER)
    public ResponseVO<ReturnPageVO<Comment>> getCommentListByTopCommentId(@RequestParam("topCommentId") Integer topCommentId,
                                                                          @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                                                          @RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
                                                                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                                          @RequestParam(value = "sorter", required = false, defaultValue = "{\"update_time\":\"descend\"}") String sorter) {
        AssertionUtil.notNull(topCommentId, ErrorCode.BIZ_PARAM_ILLEGAL, COMMENT_NOT_EXIST_ERROR);
        if (commentService.getCommentById(topCommentId) == null) {
            return ResponseVO.createErr(COMMENT_NOT_EXIST_ERROR);
        }
        ListPageUtil.paging(current, pageSize, sorter);
        List<Comment> commentList = commentService.getCommentListByTopCommentId(topCommentId, keyword);
        PageInfo<Comment> pageInfo = new PageInfo<>(commentList);
        ReturnPageVO<Comment> returnPageVO = ListPageUtil.returnPage(pageInfo);
        return ResponseVO.createSuc(returnPageVO);
    }
}

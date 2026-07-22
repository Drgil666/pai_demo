package com.example.pai_demo.controller;

import com.example.pai_demo.annoations.Authorize;
import com.example.pai_demo.exception.ErrorCode;
import com.example.pai_demo.model.ArticleDetail;
import com.example.pai_demo.model.vo.ResponseVO;
import com.example.pai_demo.model.vo.ReturnPageVO;
import com.example.pai_demo.service.ArticleDetailService;
import com.example.pai_demo.service.ArticleService;
import com.example.pai_demo.utils.AssertionUtil;
import com.example.pai_demo.utils.ListPageUtil;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static com.example.pai_demo.constants.errorDict.*;

/**
 * @author GilbertYoung
 * @date 2026/07/21 15:17
 */
@RestController
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("/api/article_detail")
public class ArticleDetailController {
    @Resource
    private ArticleDetailService articleDetailService;
    @Resource
    private ArticleService articleService;

    @PostMapping()
    @ApiOperation(value = "创建文章内容", notes = "创建文章内容")
    @Authorize(value = Authorize.USER)
    public ResponseVO<ArticleDetail> createArticleDetail(@RequestBody ArticleDetail articleDetail) {
        if (articleService.getArticleById(articleDetail.getArticleId()) == null) {
            return ResponseVO.createErr(ARTICLE_NOT_EXIST_ERROR);
        }
        articleDetailService.createArticleDetail(articleDetail);
        if (articleDetail.getId() != null) {
            return ResponseVO.createSuc(articleDetail);
        } else {
            return ResponseVO.createErr(CREATE_ARTICLE_DETAIL_ERROR);
        }
    }

    @PatchMapping("/{id}")
    @ApiOperation(value = "增量更新文章内容", notes = "增量更新文章内容")
    @Authorize(value = Authorize.USER)
    public ResponseVO<ArticleDetail> updateArticleDetailSelective(@PathVariable(name = "id") Integer id,
                                                                  @RequestBody ArticleDetail articleDetail) {
        if (articleDetailService.getArticleDetailById(id) == null) {
            return ResponseVO.createErr(ARTICLE_DETAIL_NOT_EXIST_ERROR);
        }
        articleDetail.setId(id);
        if (articleDetailService.updateArticleDetailSelective(articleDetail) == 1) {
            return ResponseVO.createSuc(articleDetailService.getArticleDetailById(id));
        } else {
            return ResponseVO.createErr(UPDATE_ERROR);
        }
    }

    @PostMapping("/{id}")
    @ApiOperation(value = "全量更新文章内容", notes = "全量更新文章内容")
    @Authorize(value = Authorize.USER)
    public ResponseVO<ArticleDetail> updateArticleDetailAll(@PathVariable(name = "id") Integer id,
                                                            @RequestBody ArticleDetail articleDetail) {
        if (articleDetailService.getArticleDetailById(id) == null) {
            return ResponseVO.createErr(ARTICLE_DETAIL_NOT_EXIST_ERROR);
        }
        articleDetail.setId(id);
        if (articleDetailService.updateArticleDetailAll(articleDetail) == 1) {
            return ResponseVO.createSuc(articleDetailService.getArticleDetailById(id));
        } else {
            return ResponseVO.createErr(UPDATE_ERROR);
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取文章内容", notes = "根据id获取文章内容")
    public ResponseVO<ArticleDetail> getArticleDetailById(@PathVariable(name = "id") Integer id) {
        ArticleDetail articleDetail = articleDetailService.getArticleDetailById(id);
        if (articleDetail != null) {
            return ResponseVO.createSuc(articleDetail);
        } else {
            return ResponseVO.createErr(ARTICLE_DETAIL_NOT_EXIST_ERROR);
        }
    }

    @GetMapping("/article_id")
    @ApiOperation(value = "根据文章id获取最新版本的文章内容", notes = "根据文章id获取最新版本的文章内容")
    public ResponseVO<ArticleDetail> getArticleDetailByArticleId(@RequestParam("articleId") Integer articleId) {
        AssertionUtil.notNull(articleId, ErrorCode.BIZ_PARAM_ILLEGAL, ARTICLE_NOT_EXIST_ERROR);
        if (articleService.getArticleById(articleId) == null) {
            return ResponseVO.createErr(ARTICLE_NOT_EXIST_ERROR);
        }
        ArticleDetail articleDetail = articleDetailService.getArticleDetailByArticleId(articleId);
        if (articleDetail != null) {
            return ResponseVO.createSuc(articleDetail);
        } else {
            return ResponseVO.createErr(ARTICLE_DETAIL_NOT_EXIST_ERROR);
        }
    }

    @GetMapping()
    @ApiOperation(value = "根据文章id获取所有版本的文章内容列表", notes = "根据文章id获取所有版本的文章内容列表")
    @Authorize(value = Authorize.USER)
    public ResponseVO<ReturnPageVO<ArticleDetail>> getArticleDetailListByArticleId(@RequestParam("articleId") Integer articleId,
                                                                                   @RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
                                                                                   @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                                                   @RequestParam(value = "sorter", required = false, defaultValue = "{\"update_time\":\"descend\"}") String sorter) {
        AssertionUtil.notNull(articleId, ErrorCode.BIZ_PARAM_ILLEGAL, ARTICLE_NOT_EXIST_ERROR);
        if (articleService.getArticleById(articleId) == null) {
            return ResponseVO.createErr(ARTICLE_NOT_EXIST_ERROR);
        }
        ListPageUtil.paging(current, pageSize, sorter);
        List<ArticleDetail> articleDetailList = articleDetailService.getArticleDetailListByArticleId(articleId);
        PageInfo<ArticleDetail> pageInfo = new PageInfo<>(articleDetailList);
        ReturnPageVO<ArticleDetail> returnPageVO = ListPageUtil.returnPage(pageInfo);
        return ResponseVO.createSuc(returnPageVO);
    }
}

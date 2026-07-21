package com.example.pai_demo.controller;

import com.example.pai_demo.annoations.Authorize;
import com.example.pai_demo.exception.ErrorCode;
import com.example.pai_demo.model.ArticleTag;
import com.example.pai_demo.model.Tag;
import com.example.pai_demo.model.vo.ResponseVO;
import com.example.pai_demo.model.vo.ReturnPageVO;
import com.example.pai_demo.service.ArticleService;
import com.example.pai_demo.service.ArticleTagService;
import com.example.pai_demo.service.TagService;
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
@RequestMapping("/api/article_tag")
public class ArticleTagController {
    @Resource
    private ArticleTagService articleTagService;
    @Resource
    private ArticleService articleService;
    @Resource
    private TagService tagService;

    @PostMapping()
    @ApiOperation(value = "创建文章标签关联", notes = "创建文章标签关联")
    @Authorize(value = Authorize.USER)
    public ResponseVO<ArticleTag> createArticleTag(@RequestBody ArticleTag articleTag) {
        //TODO:这里改为DTO，批量生成文章标签
        if (articleService.getArticleById(articleTag.getArticleId()) == null) {
            return ResponseVO.createErr(ARTICLE_NOT_EXIST_ERROR);
        }
        if (tagService.getTagById(articleTag.getTagId()) == null) {
            return ResponseVO.createErr(TAG_NOT_EXIST_ERROR);
        }
        if (articleTagService.getArticleTagByArticleIdAndTagId(articleTag.getArticleId(), articleTag.getTagId()) != null) {
            return ResponseVO.createErr(ARTICLE_TAG_EXIST_ERROR);
        }
        articleTagService.createArticleTag(articleTag);
        if (articleTag.getId() != null) {
            return ResponseVO.createSuc(articleTag);
        } else {
            return ResponseVO.createErr(CREATE_ARTICLE_TAG_ERROR);
        }
    }

    @PatchMapping("/{id}")
    @ApiOperation(value = "增量更新文章标签关联", notes = "增量更新文章标签关联")
    @Authorize(value = Authorize.USER)
    public ResponseVO<ArticleTag> updateArticleTagSelective(@PathVariable(name = "id") Integer id,
                                                            @RequestBody ArticleTag articleTag) {
        if (articleTagService.getArticleTagById(id) == null) {
            return ResponseVO.createErr(ARTICLE_TAG_NOT_EXIST_ERROR);
        }
        articleTag.setId(id);
        if (articleTagService.updateArticleTagSelective(articleTag) == 1) {
            return ResponseVO.createSuc(articleTagService.getArticleTagById(id));
        } else {
            return ResponseVO.createErr(UPDATE_ERROR);
        }
    }

    @PostMapping("/{id}")
    @ApiOperation(value = "全量更新文章标签关联", notes = "全量更新文章标签关联")
    @Authorize(value = Authorize.USER)
    public ResponseVO<ArticleTag> updateArticleTagAll(@PathVariable(name = "id") Integer id,
                                                      @RequestBody ArticleTag articleTag) {
        if (articleTagService.getArticleTagById(id) == null) {
            return ResponseVO.createErr(ARTICLE_TAG_NOT_EXIST_ERROR);
        }
        articleTag.setId(id);
        if (articleTagService.updateArticleTagAll(articleTag) == 1) {
            return ResponseVO.createSuc(articleTagService.getArticleTagById(id));
        } else {
            return ResponseVO.createErr(UPDATE_ERROR);
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取文章标签关联", notes = "根据id获取文章标签关联")
    public ResponseVO<ArticleTag> getArticleTagById(@PathVariable(name = "id") Integer id) {
        ArticleTag articleTag = articleTagService.getArticleTagById(id);
        if (articleTag != null) {
            return ResponseVO.createSuc(articleTag);
        } else {
            return ResponseVO.createErr(ARTICLE_TAG_NOT_EXIST_ERROR);
        }
    }

    @GetMapping()
    @ApiOperation(value = "根据文章id获取对应的标签列表", notes = "根据文章id获取对应的标签列表")
    public ResponseVO<ReturnPageVO<Tag>> getTagListByArticleId(@RequestParam("articleId") Integer articleId,
                                                               @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                                               @RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
                                                               @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                               @RequestParam(value = "sorter", required = false, defaultValue = "{\"update_time\":\"descend\"}") String sorter) {
        AssertionUtil.notNull(articleId, ErrorCode.BIZ_PARAM_ILLEGAL, ARTICLE_NOT_EXIST_ERROR);
        if (articleService.getArticleById(articleId) == null) {
            return ResponseVO.createErr(ARTICLE_NOT_EXIST_ERROR);
        }
        ListPageUtil.paging(current, pageSize, sorter);
        List<Tag> tagList = articleTagService.getTagListByArticleId(articleId, keyword);
        PageInfo<Tag> pageInfo = new PageInfo<>(tagList);
        ReturnPageVO<Tag> returnPageVO = ListPageUtil.returnPage(pageInfo);
        return ResponseVO.createSuc(returnPageVO);
    }
}

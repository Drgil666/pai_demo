package com.example.pai_demo.controller;

import com.example.pai_demo.annoations.Authorize;
import com.example.pai_demo.exception.ErrorCode;
import com.example.pai_demo.model.Tag;
import com.example.pai_demo.model.vo.ResponseVO;
import com.example.pai_demo.model.vo.ReturnPageVO;
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
 * @date 2026/07/14 16:14
 */
@RestController
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("/api/tag")
public class TagController {
    @Resource
    private TagService tagService;

    @PostMapping()
    @ApiOperation(value = "创建标签", notes = "创建标签")
    @Authorize(value = Authorize.USER)
    public ResponseVO<Tag> createTag(@RequestBody Tag tag) {
        AssertionUtil.notNull(tag.getName(), ErrorCode.BIZ_PARAM_ILLEGAL, TAG_NAME_NULL_ERROR);
        if (tagService.getTagByName(tag.getName()) != null) {
            return ResponseVO.createErr(TAG_NAME_EXIST_ERROR);
        }
        tagService.createTag(tag);
        if (tag.getId() != null) {
            return ResponseVO.createSuc(tag);
        } else {
            return ResponseVO.createErr(CREATE_TAG_ERROR);
        }
    }

    @PatchMapping("/{id}")
    @ApiOperation(value = "增量更新标签", notes = "增量更新标签")
    @Authorize(value = Authorize.USER)
    public ResponseVO<Tag> updateTagSelective(@PathVariable(name = "id") Integer id,
                                              @RequestBody Tag tag) {
        if (tagService.getTagById(id) == null) {
            return ResponseVO.createErr(TAG_NOT_EXIST_ERROR);
        }
        if (!tagService.getTagByName(tag.getName()).getId().equals(id)) {
            return ResponseVO.createErr(TAG_NAME_EXIST_ERROR);
        }
        tag.setId(id);
        if (tagService.updateTagSelective(tag) == 1) {
            return ResponseVO.createSuc(tagService.getTagById(id));
        } else {
            return ResponseVO.createErr(UPDATE_ERROR);
        }
    }

    @PostMapping("/{id}")
    @ApiOperation(value = "全量更新标签", notes = "全量更新标签")
    @Authorize(value = Authorize.USER)
    public ResponseVO<Tag> updateTagAll(@PathVariable(name = "id") Integer id,
                                        @RequestBody Tag tag) {
        if (tagService.getTagById(id) == null) {
            return ResponseVO.createErr(TAG_NOT_EXIST_ERROR);
        }
        if (!tagService.getTagByName(tag.getName()).getId().equals(id)) {
            return ResponseVO.createErr(TAG_NAME_EXIST_ERROR);
        }
        tag.setId(id);
        if (tagService.updateTagAll(tag) == 1) {
            return ResponseVO.createSuc(tagService.getTagById(id));
        } else {
            return ResponseVO.createErr(UPDATE_ERROR);
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取标签", notes = "根据id获取标签")
    public ResponseVO<Tag> getTagById(@PathVariable(name = "id") Integer id) {
        Tag tag = tagService.getTagById(id);
        if (tag != null) {
            return ResponseVO.createSuc(tag);
        } else {
            return ResponseVO.createErr(TAG_NOT_EXIST_ERROR);
        }
    }

    @GetMapping()
    @ApiOperation(value = "根据标签名查询标签列表", notes = "根据标签名查询标签列表")
    public ResponseVO<ReturnPageVO<Tag>> getTagListByKeyword(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                                             @RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
                                                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                             @RequestParam(value = "sorter", required = false, defaultValue = "{\"update_time\":\"descend\"}") String sorter) {

        ListPageUtil.paging(current, pageSize, sorter);
        List<Tag> tagList = tagService.getTagListByKeyword(keyword);
        PageInfo<Tag> pageInfo = new PageInfo<>(tagList);
        ReturnPageVO<Tag> returnPageVO = ListPageUtil.returnPage(pageInfo);
        return ResponseVO.createSuc(returnPageVO);
    }
}

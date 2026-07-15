package com.example.pai_demo.controller;

import com.example.pai_demo.model.Category;
import com.example.pai_demo.model.vo.ResponseVO;
import com.example.pai_demo.model.vo.ReturnPageVO;
import com.example.pai_demo.service.CategoryService;
import com.example.pai_demo.service.TokenService;
import com.example.pai_demo.service.UserService;
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
 * @date 2026/07/14 10:41
 */
@RestController
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("/api/category")
public class CategoryController {
    @Resource
    private CategoryService categoryService;
    @Resource
    private TokenService tokenService;
    @Resource
    private UserService userService;

    @PostMapping()
    @ApiOperation(value = "创建目录", notes = "创建目录")
    public ResponseVO<Category> createCategory(@RequestBody Category category) {
        if (userService.getUserById(category.getUserId()) == null) {
            return ResponseVO.createErr(USER_NOT_EXIST_ERROR);
        }
        categoryService.createCategory(category);
        if (category.getId() != null) {
            return ResponseVO.createSuc(category);
        } else {
            return ResponseVO.createErr(CREATE_CATEGORY_ERROR);
        }
    }

    @PatchMapping("/{id}")
    @ApiOperation(value = "增量更新目录", notes = "增量更新目录")
    public ResponseVO<Category> updateCategorySelective(@PathVariable(name = "id") Integer id,
                                                        @RequestBody Category category) {
        if (categoryService.getCategoryById(id) == null) {
            return ResponseVO.createErr(CATEGORY_NOT_EXIST_ERROR);
        }
        category.setId(id);
        if (categoryService.updateCategorySelective(category) == 1) {
            return ResponseVO.createSuc(categoryService.getCategoryById(id));
        } else {
            return ResponseVO.createErr(UPDATE_ERROR);
        }
    }

    @PostMapping("/{id}")
    @ApiOperation(value = "全量更新文章", notes = "全量更新文章")
    public ResponseVO<Category> updateCategoryAll(@PathVariable(name = "id") Integer id,
                                                  @RequestBody Category category) {
        if (categoryService.getCategoryById(id) == null) {
            return ResponseVO.createErr(CATEGORY_NOT_EXIST_ERROR);
        }
        category.setId(id);
        if (categoryService.updateCategoryAll(category) == 1) {
            return ResponseVO.createSuc(categoryService.getCategoryById(id));
        } else {
            return ResponseVO.createErr(UPDATE_ERROR);
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取目录", notes = "根据id获取目录")
    public ResponseVO<Category> getCategoryById(@PathVariable(name = "id") Integer id) {
        Category category = categoryService.getCategoryById(id);
        if (category != null) {
            return ResponseVO.createSuc(category);
        } else {
            return ResponseVO.createErr(ARTICLE_NOT_EXIST_ERROR);
        }
    }

    @GetMapping()
    @ApiOperation(value = "根据目录id获取文章列表", notes = "根据目录id获取文章列表")
    public ResponseVO<ReturnPageVO<Category>> getArticleListByCategoryId(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                                                         @RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
                                                                         @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                                         @RequestParam(value = "sorter", required = false, defaultValue = "{\"update_time\":\"descend\"}") String sorter) {
        ListPageUtil.paging(current, pageSize, sorter);
        List<Category> categoryList = categoryService.getCategoryListByKeyword(keyword);
        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        ReturnPageVO<Category> returnPageVO = ListPageUtil.returnPage(pageInfo);
        return ResponseVO.createSuc(returnPageVO);
    }
}

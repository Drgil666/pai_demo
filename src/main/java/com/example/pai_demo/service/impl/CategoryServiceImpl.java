package com.example.pai_demo.service.impl;

import com.example.pai_demo.mapper.CategoryMapper;
import com.example.pai_demo.model.Category;
import com.example.pai_demo.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author GilbertYoung
 * @date 2026/07/13 22:21
 */
@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    @Resource
    private CategoryMapper categoryMapper;

    /**
     * 创建目录
     *
     * @param category 创建的目录
     * @return 是否创建成功
     */
    @Override
    public Boolean createCategory(Category category) {
        category.setIsDelete(0);
        category.setCreateTime(new Date());
        category.setUpdateTime(category.getCreateTime());
        return categoryMapper.createCategory(category);
    }

    /**
     * 增量更新目录
     *
     * @param category 更新的目录
     * @return 是否更新成功
     */
    @Override
    public Long updateCategorySelective(Category category) {
        category.setUpdateTime(new Date());
        return categoryMapper.updateCategorySelective(category);
    }

    /**
     * 全量更新目录
     *
     * @param category 更新的目录
     * @return 是否更新成功
     */
    @Override
    public Long updateCategoryAll(Category category) {
        category.setUpdateTime(new Date());
        return categoryMapper.updateCategoryAll(category);
    }

    /**
     * 根据id获取目录
     *
     * @param id 目录id
     * @return 目录信息
     */
    @Override
    public Category getCategoryById(Integer id) {
        return categoryMapper.getCategoryById(id);
    }

    /**
     * 根据关键词获取目录列表
     *
     * @param keyword 关键词
     * @return 目录列表
     */
    @Override
    public List<Category> getCategoryListByKeyword(String keyword) {
        return categoryMapper.getCategoryListByKeyword(keyword);
    }
}

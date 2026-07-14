package com.example.pai_demo.service;

import com.example.pai_demo.model.Category;

import java.util.List;

/**
 * @author GilbertYoung
 * @date 2026/07/13 22:14
 */
public interface CategoryService {
    /**
     * 创建目录
     *
     * @param category 创建的目录
     * @return 是否创建成功
     */
    Boolean createCategory(Category category);

    /**
     * 增量更新目录
     *
     * @param category 更新的目录
     * @return 是否更新成功
     */
    Long updateCategorySelective(Category category);

    /**
     * 全量更新目录
     *
     * @param category 更新的目录
     * @return 是否更新成功
     */
    Long updateCategoryAll(Category category);

    /**
     * 根据id获取目录
     *
     * @param id 目录id
     * @return 目录信息
     */
    Category getCategoryById(Integer id);

    /**
     * 根据关键词获取目录列表
     *
     * @param keyword 关键词
     * @return 目录列表
     */
    List<Category> getCategoryListByKeyword(String keyword);

}

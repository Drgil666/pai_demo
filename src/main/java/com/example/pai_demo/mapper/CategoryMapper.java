package com.example.pai_demo.mapper;

import com.example.pai_demo.model.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author GilbertYoung
 * @date 2026/07/13 20:56
 */
@Mapper
public interface CategoryMapper {
    /**
     * 创建目录
     *
     * @param category 创建的目录
     * @return 是否创建成功
     */
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("insert into category (create_time, is_delete, update_time, name, user_id) values " +
            "(#{category.createTime},#{category.isDelete},#{category.updateTime},#{category.name},#{category.userId})")
    Boolean createCategory(@Param("category") Category category);

    /**
     * 增量更新目录
     *
     * @param category 更新的目录
     * @return 是否更新成功
     */
    Long updateCategorySelective(@Param("category") Category category);

    /**
     * 全量更新目录
     *
     * @param category 更新的目录
     * @return 是否更新成功
     */
    @Update("update category set update_time=#{category.updateTime}," +
            "is_delete=#{category.isDelete},update_time=#{category.updateTime}," +
            "name=#{category.name},user_id=#{category.userId} " +
            "where id=#{category.id} and is_delete=0")
    Long updateCategoryAll(@Param("category") Category category);

    /**
     * 根据id获取目录
     *
     * @param id 目录id
     * @return 目录信息
     */
    @Select("select * from category where id=#{id} and is_delete=0 LIMIT 1")
    Category getCategoryById(@Param("id") Integer id);

    /**
     * 根据关键词获取目录列表
     *
     * @param keyword 关键词
     * @return 目录列表
     */
    @Select("select * from category where name like CONCAT('%',#{keyword},'%') and is_delete=0")
    List<Category> getCategoryListByKeyword(@Param("keyword") String keyword);
}

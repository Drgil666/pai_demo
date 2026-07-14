package com.example.pai_demo.mapper;

import com.example.pai_demo.model.Tag;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author GilbertYoung
 * @date 2026/07/14 08:07
 */
@Mapper
public interface TagMapper {
    /**
     * 创建标签
     *
     * @param tag 要创建的标签
     * @return 是否创建成功
     */
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("insert into tag (name,user_id,create_time,update_time) values " +
            "(#{tag.name},#{tag.userId},#{tag.createTime},#{tag.updateTime})")
    Boolean createTag(@Param("tag") Tag tag);

    /**
     * 增量更新标签
     *
     * @param tag 要更新的标签
     * @return 是否更新成功
     */
    Long updateTagSelective(@Param("tag") Tag tag);

    /**
     * 全量更新标签
     *
     * @param tag 要更新的标签
     * @return 是否更新成功
     */
    @Update("update tag set name=#{tag.name},user_id=#{tag.userId}," +
            "is_delete=#{tag.isDelete},update_time=#{tag.updateTime} where id=#{tag.id} and is_delete=0")
    Long updateTagAll(@Param("tag") Tag tag);

    /**
     * 根据id获取标签信息
     *
     * @param id 标签id
     * @return 对应的标签信息
     */
    @Select("select * from tag where id=#{id} and is_delete=0 LIMIT 1")
    Tag getTagById(@Param("id") Integer id);

    /**
     * 根据名字获取标签信息
     *
     * @param name 关键词
     * @return 对应的标签列表
     */
    @Select("select * from tag where name=#{name} and is_delete=0 LIMIT 1")
    Tag getTagByName(@Param("name") String name);

    /**
     * 模糊查找标签列表
     *
     * @param keyword 关键词
     * @return 对应的标签列表
     */
    @Select("select * from tag where is_delete=0 and name like CONCAT('%',#{keyword},'%')")
    List<Tag> getTagListByKeyword(@Param("keyword") String keyword);
}

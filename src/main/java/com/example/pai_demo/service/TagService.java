package com.example.pai_demo.service;

import com.example.pai_demo.model.Tag;

import java.util.List;

/**
 * @author GilbertYoung
 * @date 2026/07/14 09:35
 */
public interface TagService {
    /**
     * 创建标签
     *
     * @param tag 要创建的标签
     * @return 是否创建成功
     */
    Boolean createTag(Tag tag);

    /**
     * 增量更新标签
     *
     * @param tag 要更新的标签
     * @return 是否更新成功
     */
    Long updateTagSelective(Tag tag);

    /**
     * 全量更新标签
     *
     * @param tag 要更新的标签
     * @return 是否更新成功
     */
    Long updateTagAll(Tag tag);

    /**
     * 根据id获取标签信息
     *
     * @param id 标签id
     * @return 对应的标签信息
     */
    Tag getTagById(Integer id);

    /**
     * 根据名字获取标签信息
     *
     * @param name 关键词
     * @return 对应的标签列表
     */
    Tag getTagByName(String name);

    /**
     * 模糊查找标签列表
     *
     * @param keyword 关键词
     * @return 对应的标签列表
     */
    List<Tag> getTagListByKeyword(String keyword);
}

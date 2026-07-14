package com.example.pai_demo.service.impl;

import com.example.pai_demo.mapper.TagMapper;
import com.example.pai_demo.model.Tag;
import com.example.pai_demo.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author GilbertYoung
 * @date 2026/07/14 09:35
 */
@Service
@Slf4j
public class TagServiceImpl implements TagService {
    @Resource
    private TagMapper tagMapper;

    /**
     * 创建标签
     *
     * @param tag 要创建的标签
     * @return 是否创建成功
     */
    @Override
    public Boolean createTag(Tag tag) {
        tag.setIsDelete(0);
        tag.setCreateTime(new Date());
        tag.setUpdateTime(tag.getCreateTime());
        return tagMapper.createTag(tag);
    }

    /**
     * 增量更新标签
     *
     * @param tag 要更新的标签
     * @return 是否更新成功
     */
    @Override
    public Long updateTagSelective(Tag tag) {
        tag.setUpdateTime(new Date());
        return tagMapper.updateTagSelective(tag);
    }

    /**
     * 全量更新标签
     *
     * @param tag 要更新的标签
     * @return 是否更新成功
     */
    @Override
    public Long updateTagAll(Tag tag) {
        tag.setUpdateTime(new Date());
        return tagMapper.updateTagAll(tag);
    }

    /**
     * 根据id获取标签信息
     *
     * @param id 标签id
     * @return 对应的标签信息
     */
    @Override
    public Tag getTagById(Integer id) {
        return tagMapper.getTagById(id);
    }

    /**
     * 根据名字获取标签信息
     *
     * @param name 关键词
     * @return 对应的标签列表
     */
    @Override
    public Tag getTagByName(String name) {
        return tagMapper.getTagByName(name);
    }

    /**
     * 模糊查找标签列表
     *
     * @param keyword 关键词
     * @return 对应的标签列表
     */
    @Override
    public List<Tag> getTagListByKeyword(String keyword) {
        return tagMapper.getTagListByKeyword(keyword);
    }
}

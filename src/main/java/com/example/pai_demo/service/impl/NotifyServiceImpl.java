package com.example.pai_demo.service.impl;

import com.example.pai_demo.mapper.NotifyMapper;
import com.example.pai_demo.model.Notify;
import com.example.pai_demo.service.NotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author GilbertYoung
 * @date 2026/07/22 09:35
 */
@Service
@Slf4j
public class NotifyServiceImpl implements NotifyService {
    @Resource
    private NotifyMapper notifyMapper;

    /**
     * 创建通知
     *
     * @param notify 通知
     * @return 是否创建成功
     */
    @Override
    public Boolean createNotify(Notify notify) {
        notify.setIsDelete(0);
        notify.setIsRead(0);
        notify.setCreateTime(new Date());
        notify.setUpdateTime(notify.getCreateTime());
        return notifyMapper.createNotify(notify);
    }

    /**
     * 根据id获取通知
     *
     * @param id 通知id
     * @return 通知信息
     */
    @Override
    public Notify getNotifyById(Integer id) {
        return notifyMapper.getNotifyById(id);
    }

    /**
     * 根据用户id获取通知列表
     *
     * @param userId 被通知的用户id
     * @return 通知列表
     */
    /**
     * 增量更新通知
     *
     * @param notify 通知
     * @return 影响的行数
     */
    @Override
    public Long updateNotifySelective(Notify notify) {
        notify.setUpdateTime(new Date());
        return notifyMapper.updateNotifySelective(notify);
    }

    /**
     * 全量更新通知
     *
     * @param notify 通知
     * @return 影响的行数
     */
    @Override
    public Long updateNotifyAll(Notify notify) {
        notify.setUpdateTime(new Date());
        return notifyMapper.updateNotifyAll(notify);
    }

    @Override
    public List<Notify> getNotifyListByUserId(Integer userId) {
        return notifyMapper.getNotifyListByUserId(userId);
    }
}

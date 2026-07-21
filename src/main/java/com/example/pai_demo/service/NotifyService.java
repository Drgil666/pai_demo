package com.example.pai_demo.service;

import com.example.pai_demo.model.Notify;

import java.util.List;

/**
 * @author GilbertYoung
 * @date 2026/07/22 09:35
 */
public interface NotifyService {
    /**
     * 创建通知
     *
     * @param notify 通知
     * @return 是否创建成功
     */
    Boolean createNotify(Notify notify);

    /**
     * 根据id获取通知
     *
     * @param id 通知id
     * @return 通知信息
     */
    Notify getNotifyById(Integer id);

    /**
     * 根据用户id获取通知列表
     *
     * @param userId 被通知的用户id
     * @return 通知列表
     */
    List<Notify> getNotifyListByUserId(Integer userId);
}

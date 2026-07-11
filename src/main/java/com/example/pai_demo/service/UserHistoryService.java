package com.example.pai_demo.service;

import com.example.pai_demo.model.UserHistory;

import java.util.List;

/**
 * @author GilbertYoung
 * @date 2026/07/11 16:49
 */
public interface UserHistoryService {
    /**
     * 创建用户操作流水
     *
     * @param userHistory 用户操作流水信息
     * @return 是否操作成功
     */
    Boolean createUserHistory(UserHistory userHistory);

    /**
     * 获取用户流水操作列表
     *
     * @param userId 用户id
     * @return 用户流水操作列表
     */
    List<UserHistory> getUserHistoryList(Integer userId);
}

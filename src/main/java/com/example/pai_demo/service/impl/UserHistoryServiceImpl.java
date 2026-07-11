package com.example.pai_demo.service.impl;

import com.example.pai_demo.mapper.UserHistoryMapper;
import com.example.pai_demo.model.UserHistory;
import com.example.pai_demo.service.UserHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author GilbertYoung
 * @date 2026/07/11 16:49
 */
@Service
@Slf4j
public class UserHistoryServiceImpl implements UserHistoryService {
    @Resource
    private UserHistoryMapper userHistoryMapper;

    /**
     * 创建用户操作流水
     *
     * @param userHistory 用户操作流水信息
     * @return 是否操作成功
     */
    @Override
    public Boolean createUserHistory(UserHistory userHistory) {
        userHistory.setCreateTime(new Date());
        userHistory.setUpdateTime(userHistory.getCreateTime());
        return userHistoryMapper.createUserHistory(userHistory);
    }

    /**
     * 获取用户流水操作列表
     *
     * @param userId 用户id
     * @return 用户流水操作列表
     */
    @Override
    public List<UserHistory> getUserHistoryList(Integer userId) {
        return userHistoryMapper.getUserHistoryList(userId);
    }
}

package com.example.pai_demo.controller;

import com.example.pai_demo.model.vo.ResponseVO;
import com.example.pai_demo.model.vo.UserActivityVO;
import com.example.pai_demo.service.TokenService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

import static com.example.pai_demo.constants.errorDict.ACTIVITY_RANK_ERROR;

/**
 * @author GilbertYoung
 * @date 2026/07/24 14:28
 */
@RestController
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("/api/activity_rank")
public class ActivityRankController {
    public static final Integer RANK_SIZE = 30;
    @Resource
    private TokenService tokenService;

    @GetMapping("/daily")
    @ApiOperation(value = "获取每日排行榜", notes = "获取每日排行榜")
    public ResponseVO<List<UserActivityVO>> getDailyActivityRank() {
        List<UserActivityVO> userActivityVOList = tokenService.getDailyActivityRank(RANK_SIZE);
        if (userActivityVOList != null) {
            return ResponseVO.createSuc(userActivityVOList);
        } else {
            return ResponseVO.createErr(ACTIVITY_RANK_ERROR);
        }
    }

    @GetMapping("/monthly")
    @ApiOperation(value = "获取每月排行榜", notes = "获取每月排行榜")
    public ResponseVO<List<UserActivityVO>> getMonthlyActivityRank() {
        List<UserActivityVO> userActivityVOList = tokenService.getMonthlyActivityRank(RANK_SIZE);
        if (userActivityVOList != null) {
            return ResponseVO.createSuc(userActivityVOList);
        } else {
            return ResponseVO.createErr(ACTIVITY_RANK_ERROR);
        }
    }
}

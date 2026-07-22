package com.example.pai_demo.controller;

import com.example.pai_demo.annoations.Authorize;
import com.example.pai_demo.exception.ErrorCode;
import com.example.pai_demo.model.UserHistory;
import com.example.pai_demo.model.vo.ResponseVO;
import com.example.pai_demo.model.vo.ReturnPageVO;
import com.example.pai_demo.service.TokenService;
import com.example.pai_demo.service.UserHistoryService;
import com.example.pai_demo.utils.AssertionUtil;
import com.example.pai_demo.utils.ListPageUtil;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static com.example.pai_demo.constants.errorDict.USER_NOT_EXIST_ERROR;

/**
 * @author GilbertYoung
 * @date 2026/07/11 17:04
 */
@RestController
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("/api/user_history")
public class UserHistoryController {
    @Resource
    private UserHistoryService userHistoryService;
    @Resource
    private TokenService tokenService;

    @GetMapping()
    @ApiOperation(value = "查询用户流水列表", notes = "查询用户流水列表")
    @Authorize(value = Authorize.USER)
    public ResponseVO<ReturnPageVO<UserHistory>> getFollowUserListByUserId(@RequestParam(value = "userId") Integer userId,
                                                                           @RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
                                                                           @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                                           @RequestParam(value = "sorter", required = false, defaultValue = "{\"update_time\":\"descend\"}") String sorter) {
        AssertionUtil.notNull(userId, ErrorCode.BIZ_PARAM_ILLEGAL, USER_NOT_EXIST_ERROR);
        ListPageUtil.paging(current, pageSize, sorter);
        List<UserHistory> userList = userHistoryService.getUserHistoryList(userId);
        PageInfo<UserHistory> pageInfo = new PageInfo<>(userList);
        ReturnPageVO<UserHistory> returnPageVO = ListPageUtil.returnPage(pageInfo);
        return ResponseVO.createSuc(returnPageVO);
    }
}

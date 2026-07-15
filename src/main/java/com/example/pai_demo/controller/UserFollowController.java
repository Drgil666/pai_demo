package com.example.pai_demo.controller;

import com.example.pai_demo.exception.ErrorCode;
import com.example.pai_demo.model.User;
import com.example.pai_demo.model.UserFollow;
import com.example.pai_demo.model.UserHistory;
import com.example.pai_demo.model.vo.ResponseVO;
import com.example.pai_demo.model.vo.ReturnPageVO;
import com.example.pai_demo.service.TokenService;
import com.example.pai_demo.service.UserFollowService;
import com.example.pai_demo.service.UserHistoryService;
import com.example.pai_demo.utils.AssertionUtil;
import com.example.pai_demo.utils.ListPageUtil;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static com.example.pai_demo.utils.errorDict.*;

/**
 * @author GilbertYoung
 * @date 2026/07/11 15:38
 */
@RestController
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("/api/user_follow")
public class UserFollowController {
    @Resource
    private UserFollowService userFollowService;
    @Resource
    private TokenService tokenService;
    @Resource
    private UserHistoryService userHistoryService;

    @PostMapping()
    @ApiOperation(value = "创建用户", notes = "创建用户")
    public ResponseVO<UserFollow> createUserFollow(@RequestBody UserFollow userFollow) {
        AssertionUtil.notNull(userFollow.getUserId(), ErrorCode.BIZ_PARAM_ILLEGAL, ID_NULL_ERROR);
        AssertionUtil.notNull(userFollow.getFollowId(), ErrorCode.BIZ_PARAM_ILLEGAL, ID_NULL_ERROR);
        if (userFollowService.getUserFollowByUserIdAndFollowId(userFollow.getUserId(), userFollow.getFollowId()) != null) {
            return ResponseVO.createErr(USER_FOLLOW_EXIST_ERROR);
        }
        if (userFollowService.createUserFollow(userFollow)) {
            UserHistory userHistory = new UserHistory();
            userHistory.setUserId(userFollow.getUserId());
            userHistory.setObjectId(userFollow.getFollowId());
            userHistory.setIsSubscribe(1);
            userHistoryService.createUserHistory(userHistory);
            return ResponseVO.createSuc(userFollow);
        } else {
            return ResponseVO.createErr(CREATE_USER_FOLLOW_ERROR);
        }
    }

    @PatchMapping("/{id}")
    @ApiOperation(value = "增量更新用户关注", notes = "增量更新用户关注")
    public ResponseVO<UserFollow> updateUserSelective(@PathVariable(name = "id") Integer id,
                                                      @RequestBody UserFollow userFollow) {
        if (userFollowService.getUserFollowById(id) == null) {
            return ResponseVO.createErr(USER_FOLLOW_NOT_EXIST_ERROR);
        }
        userFollow.setId(id);
        if (userFollowService.updateUserFollowSelective(userFollow) == 1) {
            UserFollow newUserFollow = userFollowService.getUserFollowById(id);
            UserHistory userHistory = new UserHistory();
            userHistory.setUserId(newUserFollow.getUserId());
            userHistory.setObjectId(newUserFollow.getFollowId());
            userHistory.setIsSubscribe(2);
            userHistoryService.createUserHistory(userHistory);
            return ResponseVO.createSuc(userFollowService.getUserFollowById(id));
        } else {
            return ResponseVO.createErr(UPDATE_ERROR);
        }
    }

    @GetMapping()
    @ApiOperation(value = "根据昵称查询关注用户列表", notes = "根据昵称查询关注用户列表")
    public ResponseVO<ReturnPageVO<User>> getFollowUserListByUserId(@RequestHeader(value = "token", required = false) String token,
                                                                    @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                                                    @RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
                                                                    @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                                    @RequestParam(value = "sorter", required = false, defaultValue = "{\"update_time\":\"descend\"}") String sorter) {
        Integer userId = tokenService.getUserIdByToken(token);
        ListPageUtil.paging(current, pageSize, sorter);
        List<User> userList = userFollowService.getFollowUserListByUserId(userId, keyword);
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        ReturnPageVO<User> returnPageVO = ListPageUtil.returnPage(pageInfo);
        return ResponseVO.createSuc(returnPageVO);
    }
}

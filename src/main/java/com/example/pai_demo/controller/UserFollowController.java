package com.example.pai_demo.controller;

import com.example.pai_demo.annoations.Authorize;
import com.example.pai_demo.annoations.CurrentUser;
import com.example.pai_demo.enums.UserStatisticEventEnum;
import com.example.pai_demo.exception.ErrorCode;
import com.example.pai_demo.model.Notify;
import com.example.pai_demo.model.User;
import com.example.pai_demo.model.UserFollow;
import com.example.pai_demo.model.UserHistory;
import com.example.pai_demo.model.event.UserStatisticEvent;
import com.example.pai_demo.model.message.NotifyEventMessage;
import com.example.pai_demo.model.vo.ResponseVO;
import com.example.pai_demo.model.vo.ReturnPageVO;
import com.example.pai_demo.service.UserFollowService;
import com.example.pai_demo.service.UserHistoryService;
import com.example.pai_demo.utils.AssertionUtil;
import com.example.pai_demo.utils.ListPageUtil;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static com.example.pai_demo.constants.NotifyConstant.NOTIFY_SUBSCRIBE_CONTENT;
import static com.example.pai_demo.constants.errorDict.*;

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
    private UserHistoryService userHistoryService;
    @Resource
    private ApplicationEventPublisher eventPublisher;

    @PostMapping()
    @ApiOperation(value = "创建用户", notes = "创建用户")
    @Authorize(value = Authorize.USER)
    public ResponseVO<UserFollow> createUserFollow(@RequestBody UserFollow userFollow) {
        AssertionUtil.notNull(userFollow.getUserId(), ErrorCode.BIZ_PARAM_ILLEGAL, ID_NULL_ERROR);
        AssertionUtil.notNull(userFollow.getFollowId(), ErrorCode.BIZ_PARAM_ILLEGAL, ID_NULL_ERROR);
        if (userFollowService.getUserFollowByUserIdAndFollowId(userFollow.getUserId(), userFollow.getFollowId()) != null) {
            return ResponseVO.createErr(USER_FOLLOW_EXIST_ERROR);
        }
        if (userFollowService.createUserFollow(userFollow)) {
            //关注用户时redis同步缓存,为双方用户都更新统计
            UserStatisticEvent userStatisticEvent1 = new UserStatisticEvent();
            userStatisticEvent1.setUserId(userFollow.getUserId());
            userStatisticEvent1.setType(UserStatisticEventEnum.USER_FOLLOW);
            eventPublisher.publishEvent(userStatisticEvent1);
            UserStatisticEvent userStatisticEvent2 = new UserStatisticEvent();
            userStatisticEvent2.setUserId(userFollow.getFollowId());
            userStatisticEvent2.setType(UserStatisticEventEnum.USER_FOLLOWER);
            eventPublisher.publishEvent(userStatisticEvent2);
            //创建用户操作流水
            UserHistory userHistory = new UserHistory();
            userHistory.setUserId(userFollow.getUserId());
            userHistory.setObjectId(userFollow.getFollowId());
            userHistory.setIsSubscribe(1);
            userHistoryService.createUserHistory(userHistory);
            //创建通知
            Notify notify = new Notify();
            notify.setType(Notify.NOTIFY_FAVORITE);
            notify.setOperateUserId(userFollow.getUserId());
            notify.setContent(NOTIFY_SUBSCRIBE_CONTENT);
            notify.setNotifyUserId(userFollow.getFollowId());
            eventPublisher.publishEvent(new NotifyEventMessage(notify));
            return ResponseVO.createSuc(userFollow);
        } else {
            return ResponseVO.createErr(CREATE_USER_FOLLOW_ERROR);
        }
    }

    @PatchMapping("/{id}")
    @ApiOperation(value = "增量更新用户关注", notes = "增量更新用户关注")
    @Authorize(value = Authorize.USER)
    public ResponseVO<UserFollow> updateUserSelective(@PathVariable(name = "id") Integer id,
                                                      @RequestBody UserFollow userFollow) {
        if (userFollowService.getUserFollowById(id) == null) {
            return ResponseVO.createErr(USER_FOLLOW_NOT_EXIST_ERROR);
        }
        userFollow.setId(id);
        if (userFollowService.updateUserFollowSelective(userFollow) == 1) {
            UserFollow newUserFollow = userFollowService.getUserFollowById(id);
            //创建用户操作流水
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
    @Authorize(value = Authorize.USER)
    public ResponseVO<ReturnPageVO<User>> getFollowUserListByUserId(@CurrentUser Integer userId,
                                                                    @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                                                    @RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
                                                                    @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                                    @RequestParam(value = "sorter", required = false, defaultValue = "{\"update_time\":\"descend\"}") String sorter) {
        ListPageUtil.paging(current, pageSize, sorter);
        List<User> userList = userFollowService.getFollowUserListByUserId(userId, keyword);
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        ReturnPageVO<User> returnPageVO = ListPageUtil.returnPage(pageInfo);
        return ResponseVO.createSuc(returnPageVO);
    }
}

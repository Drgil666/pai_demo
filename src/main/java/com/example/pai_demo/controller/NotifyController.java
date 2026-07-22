package com.example.pai_demo.controller;

import com.example.pai_demo.annoations.Authorize;
import com.example.pai_demo.exception.ErrorCode;
import com.example.pai_demo.model.Notify;
import com.example.pai_demo.model.vo.ResponseVO;
import com.example.pai_demo.model.vo.ReturnPageVO;
import com.example.pai_demo.service.NotifyService;
import com.example.pai_demo.service.UserService;
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
 * @date 2026/07/22 09:35
 */
@RestController
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("/api/notify")
public class NotifyController {
    @Resource
    private NotifyService notifyService;
    @Resource
    private UserService userService;

    @PostMapping()
    @ApiOperation(value = "创建通知", notes = "创建通知")
    @Authorize(Authorize.ADMIN)
    public ResponseVO<Notify> createNotify(@RequestBody Notify notify) {
        //TODO:要在其他的Controller层中补全Notify的创建！
        notify.setType(Notify.NOTIFY_SYSTEM);
        if (userService.getUserById(notify.getNotifyUserId()) == null ||
                userService.getUserById(notify.getOperateUserId()) == null) {
            return ResponseVO.createErr(USER_NOT_EXIST_ERROR);
        }
        notifyService.createNotify(notify);
        if (notify.getId() != null) {
            return ResponseVO.createSuc(notify);
        } else {
            return ResponseVO.createErr(NOTIFY_NOT_EXIST_ERROR);
        }
    }

    @PatchMapping("/{id}")
    @ApiOperation(value = "增量更新通知", notes = "增量更新通知，通常用于标记已读")
    @Authorize(value = Authorize.USER)
    public ResponseVO<Notify> updateNotifySelective(@PathVariable(name = "id") Integer id,
                                                    @RequestBody Notify notify) {
        if (notifyService.getNotifyById(id) == null) {
            return ResponseVO.createErr(NOTIFY_NOT_EXIST_ERROR);
        }
        notify.setId(id);
        if (notifyService.updateNotifySelective(notify) == 1) {
            return ResponseVO.createSuc(notifyService.getNotifyById(id));
        } else {
            return ResponseVO.createErr(UPDATE_ERROR);
        }
    }

    @PostMapping("/{id}")
    @ApiOperation(value = "全量更新通知", notes = "全量更新通知")
    @Authorize(value = Authorize.USER)
    public ResponseVO<Notify> updateNotifyAll(@PathVariable(name = "id") Integer id,
                                              @RequestBody Notify notify) {
        if (notifyService.getNotifyById(id) == null) {
            return ResponseVO.createErr(NOTIFY_NOT_EXIST_ERROR);
        }
        notify.setId(id);
        if (notifyService.updateNotifyAll(notify) == 1) {
            return ResponseVO.createSuc(notifyService.getNotifyById(id));
        } else {
            return ResponseVO.createErr(UPDATE_ERROR);
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取通知", notes = "根据id获取通知")
    @Authorize(value = Authorize.USER)
    public ResponseVO<Notify> getNotifyById(@PathVariable(name = "id") Integer id) {
        Notify notify = notifyService.getNotifyById(id);
        if (notify != null) {
            return ResponseVO.createSuc(notify);
        } else {
            return ResponseVO.createErr(NOTIFY_NOT_EXIST_ERROR);
        }
    }

    @GetMapping()
    @ApiOperation(value = "根据用户id获取通知列表", notes = "根据用户id获取通知列表")
    @Authorize(value = Authorize.USER)
    public ResponseVO<ReturnPageVO<Notify>> getNotifyListByUserId(@RequestParam("userId") Integer userId,
                                                                  @RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
                                                                  @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                                  @RequestParam(value = "sorter", required = false, defaultValue = "{\"update_time\":\"descend\"}") String sorter) {
        AssertionUtil.notNull(userId, ErrorCode.BIZ_PARAM_ILLEGAL, USER_NOT_EXIST_ERROR);
        if (userService.getUserById(userId) == null) {
            return ResponseVO.createErr(USER_NOT_EXIST_ERROR);
        }
        ListPageUtil.paging(current, pageSize, sorter);
        List<Notify> notifyList = notifyService.getNotifyListByUserId(userId);
        PageInfo<Notify> pageInfo = new PageInfo<>(notifyList);
        ReturnPageVO<Notify> returnPageVO = ListPageUtil.returnPage(pageInfo);
        return ResponseVO.createSuc(returnPageVO);
    }
}

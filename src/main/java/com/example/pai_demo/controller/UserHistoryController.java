package com.example.pai_demo.controller;

import com.example.pai_demo.model.UserHistory;
import com.example.pai_demo.model.vo.Response;
import com.example.pai_demo.model.vo.ReturnPage;
import com.example.pai_demo.service.TokenService;
import com.example.pai_demo.service.UserHistoryService;
import com.example.pai_demo.utils.ListPageUtil;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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
    @ApiOperation(value = "根据昵称查询关注用户列表", notes = "根据昵称查询关注用户列表")
    public Response<ReturnPage<UserHistory>> getFollowUserListByUserId(@RequestHeader(value = "token", required = false) String token,
                                                                       @RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
                                                                       @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                                       @RequestParam(value = "sorter", required = false, defaultValue = "{\"update_time\":\"descend\"}") String sorter) {
        Integer userId = tokenService.getUserIdByToken(token);
        ListPageUtil.paging(current, pageSize, sorter);
        List<UserHistory> userList = userHistoryService.getUserHistoryList(userId);
        PageInfo<UserHistory> pageInfo = new PageInfo<>(userList);
        ReturnPage<UserHistory> returnPage = ListPageUtil.returnPage(pageInfo);
        return Response.createSuc(returnPage);
    }
}

package com.example.pai_demo.controller;

import com.example.pai_demo.annoations.Authorize;
import com.example.pai_demo.enums.ActivityRankStatisticEventEnum;
import com.example.pai_demo.model.User;
import com.example.pai_demo.model.event.ActivityRankStatisticEvent;
import com.example.pai_demo.model.vo.LoginUserVO;
import com.example.pai_demo.model.vo.LoginVO;
import com.example.pai_demo.model.vo.ResponseVO;
import com.example.pai_demo.model.vo.ReturnPageVO;
import com.example.pai_demo.service.TokenService;
import com.example.pai_demo.service.UserService;
import com.example.pai_demo.utils.ListPageUtil;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static com.example.pai_demo.constants.errorDict.*;

/**
 * @author GilbertYoung
 * @date 2026/07/07 16:14
 */
@RestController
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("/api/user")
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private TokenService tokenService;
    @Resource
    private ApplicationEventPublisher eventPublisher;

    @PostMapping()
    @ApiOperation(value = "创建用户", notes = "创建用户")
    public ResponseVO<User> createUser(@RequestBody User user) {
        if (userService.getUserByUsername(user.getUsername()) != null) {
            return ResponseVO.createErr(USERNAME_EXIST_ERROR);
        }
        userService.createUser(user);
        if (user.getId() != null) {
            return ResponseVO.createSuc(user);
        } else {
            return ResponseVO.createErr(CREATE_USER_ERROR);
        }
    }

    @PatchMapping("/{id}")
    @ApiOperation(value = "增量更新用户", notes = "增量更新用户")
    @Authorize(value = Authorize.USER)
    public ResponseVO<User> updateUserSelective(@PathVariable(name = "id") Integer id,
                                                @RequestBody User user) {
        if (userService.getUserById(id) == null) {
            return ResponseVO.createErr(USER_NOT_EXIST_ERROR);
        }
        user.setId(id);
        if (userService.updateUserSelective(user) == 1) {
            return ResponseVO.createSuc(userService.getUserById(id));
        } else {
            return ResponseVO.createErr(UPDATE_ERROR);
        }
    }

    @PostMapping("/{id}")
    @ApiOperation(value = "全量更新用户", notes = "全量更新用户")
    @Authorize(value = Authorize.USER)
    public ResponseVO<User> updateUserAll(@PathVariable(name = "id") Integer id,
                                          @RequestBody User user) {
        if (userService.getUserById(id) == null) {
            return ResponseVO.createErr(USER_NOT_EXIST_ERROR);
        }
        user.setId(id);
        if (userService.updateUserAll(user) == 1) {
            return ResponseVO.createSuc(userService.getUserById(id));
        } else {
            return ResponseVO.createErr(UPDATE_ERROR);
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取用户", notes = "根据id获取用户")
    public ResponseVO<User> getUserById(@PathVariable(name = "id") Integer id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseVO.createSuc(user);
        } else {
            return ResponseVO.createErr(USER_NOT_EXIST_ERROR);
        }
    }

    @GetMapping()
    @ApiOperation(value = "根据昵称查询用户列表", notes = "根据昵称查询用户列表")
    public ResponseVO<ReturnPageVO<User>> getUserListByKeyword(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                                               @RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
                                                               @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                               @RequestParam(value = "sorter", required = false, defaultValue = "{\"update_time\":\"descend\"}") String sorter) {

        ListPageUtil.paging(current, pageSize, sorter);
        List<User> userList = userService.getUserListByKeyword(keyword);
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        ReturnPageVO<User> returnPageVO = ListPageUtil.returnPage(pageInfo);
        return ResponseVO.createSuc(returnPageVO);
    }

    @PostMapping("/login")
    @ApiOperation(value = "登录", notes = "登录")
    public ResponseVO<LoginUserVO> login(@RequestBody LoginVO loginVO) {
        if (loginVO.getUsername() == null || loginVO.getPassword() == null) {
            return ResponseVO.createErr(EMPTY_USERNAME_OR_PASSWORD_ERROR);
        }
        User user = userService.getUserByUsername(loginVO.getUsername());
        if (user == null) {
            return ResponseVO.createErr(LOGIN_ERROR);
        }
        if (passwordEncoder.matches(loginVO.getPassword(), user.getPassword())) {
            String token = tokenService.generateToken(user.getId());
            LoginUserVO loginUserVO = new LoginUserVO();
            loginUserVO.setPrivilege(user.getPrivilege());
            loginUserVO.setToken(token);
            loginUserVO.setUserId(user.getId());
            //更新用户活跃度
            ActivityRankStatisticEvent activityRankStatisticEvent = new ActivityRankStatisticEvent();
            activityRankStatisticEvent.setUserId(user.getId());
            activityRankStatisticEvent.setType(ActivityRankStatisticEventEnum.USER_LOGIN);
            eventPublisher.publishEvent(activityRankStatisticEvent);
            return ResponseVO.createSuc(loginUserVO);
        } else {
            return ResponseVO.createErr(LOGIN_ERROR);
        }
    }

    @ApiOperation(value = "测试接口", notes = "测试接口")
    @GetMapping("/check")
    public String check() {
        return "ok2";
    }
}

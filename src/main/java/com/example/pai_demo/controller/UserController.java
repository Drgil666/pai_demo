package com.example.pai_demo.controller;

import com.example.pai_demo.model.User;
import com.example.pai_demo.model.vo.LoginUserVO;
import com.example.pai_demo.model.vo.LoginVO;
import com.example.pai_demo.model.vo.Response;
import com.example.pai_demo.model.vo.ReturnPage;
import com.example.pai_demo.service.TokenService;
import com.example.pai_demo.service.UserService;
import com.example.pai_demo.utils.ListPageUtil;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static com.example.pai_demo.utils.errorDict.*;

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

    @PostMapping()
    @ApiOperation(value = "创建用户", notes = "创建用户")
    public Response<User> createUser(@RequestBody User user) {
        if (userService.getUserByUsername(user.getUsername()) != null) {
            return Response.createErr(EXIST_USERNAME_ERROR);
        }
        userService.createUser(user);
        if (user.getId() != null) {
            return Response.createSuc(user);
        } else {
            return Response.createErr(CREATE_USER_ERROR);
        }
    }

    @PatchMapping("/{id}")
    public Response<User> updateUserSelective(@PathVariable(name = "id") Integer id,
                                              @RequestBody User user) {
        if (userService.getUserById(id) == null) {
            return Response.createErr(USER_NOT_EXIST_ERROR);
        }
        user.setId(id);
        if (userService.updateUserSelective(user) == 1) {
            return Response.createSuc(user);
        } else {
            return Response.createErr(UPDATE_USER_ERROR);
        }
    }

    @PostMapping("/{id}")
    public Response<User> updateUserAll(@PathVariable(name = "id") Integer id,
                                        @RequestBody User user) {
        if (userService.getUserById(id) == null) {
            return Response.createErr(USER_NOT_EXIST_ERROR);
        }
        user.setId(id);
        if (userService.updateUserAll(user) == 1) {
            return Response.createSuc(user);
        } else {
            return Response.createErr(UPDATE_USER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public Response<User> getUserById(@PathVariable(name = "id") Integer id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return Response.createSuc(user);
        } else {
            return Response.createErr(USER_NOT_EXIST_ERROR);
        }
    }

    @GetMapping()
    public Response<ReturnPage<User>> getUserListByKeyword(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                                           @RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
                                                           @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                           @RequestParam(value = "sorter", required = false, defaultValue = "{\"update_time\":\"descend\"}") String sorter) {

        ListPageUtil.paging(current, pageSize, sorter);
        List<User> userList = userService.getUserListByKeyword(keyword);
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        ReturnPage<User> returnPage = ListPageUtil.returnPage(pageInfo);
        return Response.createSuc(returnPage);
    }

    @PostMapping("/login")
    public Response<LoginUserVO> login(@RequestBody LoginVO loginVO) {
        if (loginVO.getUsername() == null || loginVO.getPassword() == null) {
            return Response.createErr(EMPTY_USERNAME_OR_PASSWORD_ERROR);
        }
        User user = userService.getUserByUsername(loginVO.getUsername());
        if (user == null) {
            return Response.createErr(USER_NOT_EXIST_ERROR);
        }
        if (passwordEncoder.matches(loginVO.getPassword(), user.getPassword())) {
            String token = tokenService.generateToken(user.getId());
            LoginUserVO loginUserVO = new LoginUserVO();
            loginUserVO.setPrivilege(user.getPrivilege());
            loginUserVO.setToken(token);
            return Response.createSuc(loginUserVO);
        } else {
            return Response.createErr(LOGIN_ERROR);
        }
    }

    @ApiOperation(value = "测试接口", notes = "测试接口")
    @GetMapping("/check")
    public String check() {
        return "ok2";
    }
}

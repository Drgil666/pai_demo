package com.example.pai_demo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author GilbertYoung
 * @date 2026/07/07 16:14
 */
@RestController
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("/api/user")
public class UserController {

    @ApiOperation(value = "测试接口", notes = "测试接口")
    @GetMapping("/check")
    public String check() {
        return "ok2";
    }
}

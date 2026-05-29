package com.company.management.controller;

import com.company.management.entity.Result;
import com.company.management.service.UserService;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Validated
@Tag(name = "用户管理")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public Result register(@Parameter(description = "用户名") @RequestParam("username")String username,
                           @Parameter(description = "密码") @RequestParam("password")String password) {
        return userService.register(username, password);
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result login(@Parameter(description = "用户名") @RequestParam("username")String username,
                        @Parameter(description = "密码") @RequestParam("password")String password) {
        return userService.login(username, password);
    }

    @PostMapping("info")
    @Operation(summary = "获取用户信息")
    public Result info() {
        return userService.info();
    }

    @PostMapping("logout")
    @Operation(summary = "用户登出")
    public Result logout() {
        return userService.logout();
    }

    @PostMapping("update")
    @Operation(summary = "修改密码")
    public Result update(@Parameter(description = "旧密码") @RequestParam("oldPassword")String oldPassword,
                         @Parameter(description = "新密码") @RequestParam("newPassword")String newPassword) {
        return userService.update(oldPassword, newPassword);
    }

}

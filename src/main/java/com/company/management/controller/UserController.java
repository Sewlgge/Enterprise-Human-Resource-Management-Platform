package com.company.management.controller;

import com.company.management.annotation.OperateLog;
import com.company.management.dto.RegisterDTO;
import com.company.management.entity.Result;
import com.company.management.service.UserService;
import com.company.management.service.DeptService;
import com.company.management.service.PositionService;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    @Autowired
    private DeptService deptService;
    @Autowired
    private PositionService positionService;

    @GetMapping("/register/depts")
    @Operation(summary = "注册页-部门列表")
    public Result registerDepts() {
        return deptService.list(null);
    }

    @GetMapping("/register/positions")
    @Operation(summary = "注册页-职位列表")
    public Result registerPositions(
            @Parameter(description = "所属部门id") @RequestParam(name = "deptId", required = false) Integer deptId) {
        return positionService.list(null, deptId);
    }

    @PostMapping("/register")
    @OperateLog("用户注册")
    @Operation(summary = "用户注册")
    public Result register(@RequestBody @Validated RegisterDTO registerDTO) {
        return userService.register(registerDTO);
    }

    @PostMapping("/login")
    @OperateLog("用户登录")
    @Operation(summary = "用户登录")
    public Result login(@Parameter(description = "用户名") @RequestParam("username")String username,
                        @Parameter(description = "密码") @RequestParam("password")String password) {
        return userService.login(username, password);
    }

    @PostMapping("info")
    @OperateLog("获取用户信息")
    @Operation(summary = "获取用户信息")
    public Result info() {
        return userService.info();
    }

    @PostMapping("logout")
    @OperateLog("用户登出")
    @Operation(summary = "用户登出")
    public Result logout() {
        return userService.logout();
    }

    @PostMapping("update")
    @OperateLog("修改密码")
    @Operation(summary = "修改密码")
    public Result update(@Parameter(description = "旧密码") @RequestParam("oldPassword")String oldPassword,
                         @Parameter(description = "新密码") @RequestParam("newPassword")String newPassword) {
        return userService.update(oldPassword, newPassword);
    }

}

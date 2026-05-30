package com.company.management.controller;

import com.company.management.annotation.OperateLog;
import com.company.management.entity.Result;
import com.company.management.entity.Static;
import com.company.management.service.StaticService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashborad")
@Validated
@Tag(name = "首页面板")
public class StaticConroller {

    @Autowired
    private StaticService staticService;

    @GetMapping("/static")
    @OperateLog("查询首页总数据")
    @Operation(summary = "首页总数据")
    public Result<Static> staticborad() {
        return staticService.staticborad();
    }
}

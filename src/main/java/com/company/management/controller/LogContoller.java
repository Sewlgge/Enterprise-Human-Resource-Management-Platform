package com.company.management.controller;

import com.company.management.annotation.OperateLog;
import com.company.management.dto.LogPageDTO;
import com.company.management.entity.Log;
import com.company.management.entity.PageBean;
import com.company.management.entity.Result;
import com.company.management.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log")
@Validated
@Tag(name = "日志模块")
public class LogContoller {
    @Autowired
    private LogService logService;

    @GetMapping("/page")
    @OperateLog("分页查询日志")
    @Operation(summary = "分页查询日志")
    public Result<PageBean<Log>> page(LogPageDTO logPageDTO) {
        return logService.page(logPageDTO);
    }

}

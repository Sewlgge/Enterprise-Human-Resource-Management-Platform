package com.company.management.controller;

import com.company.management.annotation.OperateLog;
import com.company.management.dto.AttendancePageDTO;
import com.company.management.dto.AttendanceUpdateDTO;
import com.company.management.entity.Attendance;
import com.company.management.entity.PageBean;
import com.company.management.entity.Result;
import com.company.management.service.AttendanceService;
import com.company.management.vo.AttendanceVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attendance")
@Validated
@Tag(name = "考勤管理")
public class AttendanceController {
    @Autowired
    private AttendanceService attendanceService;

    @PostMapping("/signin")
    @OperateLog("签到")
    @Operation(summary = "签到")
    public Result signin() {
        return attendanceService.siginIn();
    }

    @PostMapping("/signout")
    @OperateLog("签退")
    @Operation(summary = "签退")
    public Result signout() {
        return attendanceService.siginOut();
    }

    @GetMapping("/my")
    @OperateLog("查询当前用户考勤记录")
    @Operation(summary = "查询当前用户考勤记录")
    public Result<List<Attendance>> getMyAttendance(@Parameter(description = "月份") @RequestParam(name = "month",required = false) Integer month) {
        return attendanceService.getMyAttendance(month);
    }

    @GetMapping("/page")
    @OperateLog("分页查询考勤记录")
    @Operation(summary = "分页查询考勤记录")
    public Result<PageBean<AttendanceVO>> page(@Parameter(description = "分页参数") AttendancePageDTO attendancePageDTO) {
        return attendanceService.page(attendancePageDTO);
    }

    @PutMapping("/update")
    @OperateLog("修改考勤状态")
    @Operation(summary = "修改考勤状态")
    public Result<String> update(@RequestBody @Validated AttendanceUpdateDTO attendanceUpdateDTO) {
        return attendanceService.update(attendanceUpdateDTO);
    }


}

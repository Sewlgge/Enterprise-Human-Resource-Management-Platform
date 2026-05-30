package com.company.management.controller;

import com.company.management.annotation.OperateLog;
import com.company.management.entity.Result;
import com.company.management.entity.Static;
import com.company.management.service.StaticService;
import com.company.management.vo.DashboardVO;
import com.company.management.vo.DeptEmployeeCountVO;
import com.company.management.vo.MonthAttendanceVO;
import com.company.management.vo.MonthLeaveVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dashborad")
@Validated
@Tag(name = "首页面板")
public class StaticConroller {

    @Autowired
    private StaticService staticService;

    @GetMapping
    @OperateLog("查询仪表盘聚合数据")
    @Operation(summary = "仪表盘聚合数据（概览+本月考勤+部门人数+本月请假）")
    public Result<DashboardVO> dashboard() {
        return staticService.dashboard();
    }

    @GetMapping("/static")
    @OperateLog("查询首页总数据")
    @Operation(summary = "首页概览统计")
    public Result<Static> staticborad() {
        return staticService.staticborad();
    }

    @GetMapping("/month-attendance")
    @OperateLog("查询本月考勤数据")
    @Operation(summary = "本月考勤数据")
    public Result<MonthAttendanceVO> monthAttendance() {
        return staticService.monthAttendance();
    }

    @GetMapping("/dept-employee-count")
    @OperateLog("查询各部门员工人数")
    @Operation(summary = "各部门员工人数")
    public Result<List<DeptEmployeeCountVO>> deptEmployeeCount() {
        return staticService.deptEmployeeCount();
    }

    @GetMapping("/month-leave")
    @OperateLog("查询本月请假情况")
    @Operation(summary = "本月请假情况")
    public Result<MonthLeaveVO> monthLeave() {
        return staticService.monthLeave();
    }
}

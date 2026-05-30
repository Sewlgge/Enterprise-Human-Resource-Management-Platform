package com.company.management.vo;

import com.company.management.entity.Static;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class DashboardVO {

    @Schema(description = "首页概览统计")
    private Static summary;

    @Schema(description = "本月考勤")
    private MonthAttendanceVO monthAttendance;

    @Schema(description = "各部门员工人数")
    private List<DeptEmployeeCountVO> deptEmployeeCounts;

    @Schema(description = "本月请假")
    private MonthLeaveVO monthLeave;
}

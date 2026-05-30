package com.company.management.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Static {

    @Schema(description = "员工数量")
    private Integer employeeCount;

    @Schema(description = "部门数量")
    private Integer deptCount;

    @Schema(description = "昨日考勤")
    private Integer yesterdayAttendance;

    @Schema(description = "待处理请假")
    private Integer pendingLeave;
}

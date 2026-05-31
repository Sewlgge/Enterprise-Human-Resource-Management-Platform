package com.company.management.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AttendanceVO implements Serializable {

    @Schema(description = "考勤ID")
    private Integer id;

    @Schema(description = "员工ID")
    private Integer employeeId;

    @Schema(description = "员工姓名")
    private String employeeName;

    @Schema(description = "考勤日期")
    private LocalDate attendanceDate;

    @Schema(description = "签到时间")
    private LocalDateTime signInTime;

    @Schema(description = "签退时间")
    private LocalDateTime signOutTime;

    @Schema(description = "签到状态：0=正常 1=迟到 2=早退 3=缺卡 4=请假")
    private Integer status;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}

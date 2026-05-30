package com.company.management.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AttendanceRecordVO {

    @Schema(description = "考勤ID")
    private Integer id;

    @Schema(description = "员工ID")
    private Integer employeeId;

    @Schema(description = "员工姓名")
    private String employeeName;

    @Schema(description = "考勤日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate attendanceDate;

    @Schema(description = "签到时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime signInTime;

    @Schema(description = "签退时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime signOutTime;

    @Schema(description = "状态：0正常 1迟到 2早退 3缺卡 4请假")
    private Integer status;

    @Schema(description = "备注")
    private String remark;
}

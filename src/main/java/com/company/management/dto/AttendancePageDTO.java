package com.company.management.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Schema(description = "考勤分页参数")
public class AttendancePageDTO implements Serializable {

    @Schema(description = "当前页", defaultValue = "1")
    private Integer page = 1;

    @Schema(description = "每页数量", defaultValue = "10")
    private Integer size = 10;

    @Schema(description = "员工id")
    private Integer employeeId;

    @Schema(description = "考勤状态")
    private Integer status;

    @Schema(description = "考勤日期，默认当天")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate attendanceDate;

    @Schema(description = "开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

}

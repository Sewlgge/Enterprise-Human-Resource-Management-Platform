package com.company.management.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DailyAttendanceStatVO {

    @Schema(description = "日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Schema(description = "当日考勤记录数")
    private Integer total;

    @Schema(description = "正常人数")
    private Integer normal;

    @Schema(description = "异常人数（非正常状态）")
    private Integer abnormal;
}

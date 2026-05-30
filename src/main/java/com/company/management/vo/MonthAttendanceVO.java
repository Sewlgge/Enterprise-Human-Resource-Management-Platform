package com.company.management.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class MonthAttendanceVO {

    @Schema(description = "状态统计")
    private AttendanceStatusSummaryVO summary;

    @Schema(description = "按日统计")
    private List<DailyAttendanceStatVO> dailyStats;

    @Schema(description = "本月考勤明细")
    private List<AttendanceRecordVO> records;
}

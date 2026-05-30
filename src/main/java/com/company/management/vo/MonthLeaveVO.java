package com.company.management.vo;

import com.company.management.entity.LeaveRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class MonthLeaveVO {

    @Schema(description = "状态统计")
    private LeaveStatusSummaryVO summary;

    @Schema(description = "按类型统计")
    private List<LeaveTypeStatVO> typeStats;

    @Schema(description = "本月请假记录")
    private List<LeaveRequest> records;
}

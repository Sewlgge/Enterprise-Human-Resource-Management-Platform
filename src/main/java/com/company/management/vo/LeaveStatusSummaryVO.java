package com.company.management.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LeaveStatusSummaryVO {

    @Schema(description = "本月请假申请总数")
    private Integer total;

    @Schema(description = "待审批")
    private Integer pending;

    @Schema(description = "已通过")
    private Integer approved;

    @Schema(description = "已拒绝")
    private Integer rejected;

    @Schema(description = "本月请假总天数")
    private Integer totalDays;
}

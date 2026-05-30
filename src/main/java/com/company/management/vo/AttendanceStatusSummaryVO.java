package com.company.management.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AttendanceStatusSummaryVO {

    @Schema(description = "本月考勤记录总数")
    private Integer total;

    @Schema(description = "正常")
    private Integer normal;

    @Schema(description = "迟到")
    private Integer late;

    @Schema(description = "早退")
    private Integer earlyLeave;

    @Schema(description = "缺卡")
    private Integer missing;

    @Schema(description = "请假")
    private Integer leaveCount;
}

package com.company.management.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LeaveTypeStatVO {

    @Schema(description = "请假类型")
    private String type;

    @Schema(description = "申请数量")
    private Integer count;
}

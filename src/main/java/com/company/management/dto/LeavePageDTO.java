package com.company.management.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LeavePageDTO {

    @Schema(description = "页码")
    private Integer pageNum;

    @Schema(description = "每页数量")
    private Integer pageSize;

    @Schema(description = "审核状态")
    private Integer status;

    @Schema(description = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @Schema(description = "请假人")
    private Integer employeeId;

    @Schema(description = "审核人")
    private Integer approverId;

}

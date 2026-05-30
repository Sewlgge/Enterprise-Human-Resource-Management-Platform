package com.company.management.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "日志分页查询参数")
public class LogPageDTO {

    @Schema(description = "当前页")
    private Integer pageNum;

    @Schema(description = "每页数量")
    private Integer pageSize;

    @Schema(description = "用户ID")
    private Integer userId;

    @Schema(description = "搜索关键字")
    private String keyword;

    @Schema(description = "开始时间")
    private String startTime;

    @Schema(description = "结束时间")
    private String endTime;

}

package com.company.management.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class LeaveDTO implements Serializable {

    @Schema(description = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message="请假天数不能为空")
    @Schema(description = "请假天数")
    private Integer days;

    @Schema(description = "请假类型")
    @NotBlank(message="请假类型不能为空")
    private String type;

    @Schema(description = "请假原因")
    private String reason;


}

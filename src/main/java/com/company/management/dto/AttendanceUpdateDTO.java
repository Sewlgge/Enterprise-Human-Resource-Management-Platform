package com.company.management.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class AttendanceUpdateDTO implements Serializable {

    @Schema(description = "考勤ID")
    @NotNull(message = "考勤ID不能为空")
    private Integer id;

    @Schema(description = "员工ID")
    @NotNull(message = "员工ID不能为空")
    private Integer employeeId;

    @Schema(description = "签到时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime signInTime;

    @Schema(description = "签退时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime signOutTime;

    @Schema(description = "签到状态：0=正常 1=迟到 2=早退 3=缺卡 4=请假")
    private Integer status;

}

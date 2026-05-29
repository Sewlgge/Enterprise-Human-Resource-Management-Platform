package com.company.management.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LeaveRequest implements Serializable {

    /**
    * 请假ID
    */
    @Schema(description = "请假ID")
    private Integer id;
    /**
    * 申请员工ID
    */
    @Schema(description = "申请员工ID")
    private Integer employeeId;
    /**
    * 开始日期
    */
    @Schema(description = "开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    /**
    * 结束日期
    */
    @Schema(description = "结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    /**
    * 请假天数
    */
    @Schema(description = "请假天数")
    private Integer days;
    /**
    * 请假类型
    */
    @Schema(description = "请假类型")
    private String type;
    /**
    * 请假原因
    */
    @Schema(description = "请假原因")
    private String reason;
    /**
    *
  审批状态：
  0-待审批
  1-已通过
  2-已拒绝
    */
    @Schema(description = "审核状态0-待审批 1-已通过 2-已拒绝")
    private Integer status;
    /**
    * 审批人ID
    */
    @Schema(description = "审批人ID")
    private Long approverId;
    /**
    * 审批备注
    */
    @Schema(description = "审批备注")
    private String approveRemark;
    /**
    * 申请时间
    */
    @Schema(description = "申请时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime applyTime;
    /**
    * 审批时间
    */
    @Schema(description = "审批时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime approveTime;
    /**
    * 创建时间
    */
    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
    * 更新时间
    */
    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}

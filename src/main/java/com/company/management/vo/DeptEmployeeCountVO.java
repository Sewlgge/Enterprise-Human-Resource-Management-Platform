package com.company.management.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DeptEmployeeCountVO {

    @Schema(description = "部门ID")
    private Integer deptId;

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "员工人数")
    private Integer employeeCount;
}

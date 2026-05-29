package com.company.management.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public class EmployeePageDTO implements Serializable {
    @Schema(description = "页码")
    private Integer pageNum = 1;

    @Schema(description = "每页数量")
    private Integer pageSize = 10;

    @Schema(description = "员工名称",requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String realName;

    @Schema(description = "员工手机",requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String phone;

    @Schema(description = "部门id",requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer deptId;
}

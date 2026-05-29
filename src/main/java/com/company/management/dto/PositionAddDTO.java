package com.company.management.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "添加职位请求参数")
public class PositionAddDTO implements Serializable {

    @NotNull(message = "部门ID不能为空")
    @Schema(description = "所属部门id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer deptId;

    @NotBlank(message = "职位名称不能为空")
    @Schema(description = "职位名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "职位描述")
    private String description;
}

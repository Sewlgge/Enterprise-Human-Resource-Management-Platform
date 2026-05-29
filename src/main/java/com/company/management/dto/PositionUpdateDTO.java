package com.company.management.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "职位更新参数")
public class PositionUpdateDTO implements Serializable {

    @Schema(description = "职位id")
    @NotNull(message = "职位id不能为空")
    private Integer id;

    @Schema(description = "职位名称")
    @NotBlank(message = "职位名称不能为空")
    private String name;

    @Schema(description = "职位描述")
    private String description;

}

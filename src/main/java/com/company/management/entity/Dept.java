package com.company.management.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "部门")
public class Dept implements Serializable {

    @Schema(description = "部门ID")
    private Integer id;

    @Schema(description = "部门名称")
    private String name;

    @Schema(description = "部门描述")
    private String description;

    @Schema(description = "创建时间")
    private String createTime;

    @Schema(description = "更新时间")
    private String updateTime;

    @Schema(description = "是否删除")
    private Integer isDeleted = 0;

}

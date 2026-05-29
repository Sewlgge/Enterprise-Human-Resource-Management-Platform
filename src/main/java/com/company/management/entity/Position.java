package com.company.management.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public class Position implements Serializable {

    @Schema(description = "职位ID")
    private Integer id;

    @Schema(description = "职位名称")
    private String name;

    @Schema(description = "职位描述")
    private String description;

    @Schema(description = "所属部门id")
    private Integer deptId;

    @Schema(description = "所属部门名称")
    private String deptName;

    @Schema(description = "是否删除")
    private Integer isDeleted;

    @Schema(description = "创建时间")
    private String createTime;

    @Schema(description = "更新时间")
    private String updateTime;

}

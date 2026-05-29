package com.company.management.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "职位分页查询参数")
public class PositionPageDTO implements Serializable {

    /**
     * 部门名称
     */
    @Schema(description = "职位名称")
    private String name;

    /**
     * 所属部门id
     */
    @Schema(description = "所属部门id")
    private Integer deptId;

    /**
     * 当前页
     */
    @Schema(description = "当前页")
    private Integer pageNum;

    /**
     * 每页条数
     */
    @Schema(description = "每页条数")
    private Integer pageSize;
}

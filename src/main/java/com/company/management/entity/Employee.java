package com.company.management.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Employee implements Serializable {

    @Schema(description = "员工id")
    private Integer id;

    @Schema(description = "员工姓名")
    private String realName;

    @Schema(description = "员工性别")
    private Integer gender;

    @Schema(description = "员工手机")
    private String phone;

    @Schema(description = "员工邮箱")
    private String email;

    @Schema(description = "员工部门id")
    private Integer deptId;

    @Schema(description = "员工部门名称")
    private String deptName;

    @Schema(description = "员工职位id")
    private Integer positionId;

    @Schema(description = "员工职位名称")
    private String positionName;

    @Schema(description = "员工头像")
    private String avatar;

    @Schema(description = "员工年龄")
    private Integer age;

    @Schema(description = "用户id")
    private Integer userId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "员工住址")
    private String address;

    @Schema(description = "入职时间")
    private Date hireDate;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "是否删除")
    private Integer isDeleted = 0;
}

package com.company.management.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class EmployeeUpdateDTO implements Serializable {
    @Schema(description = "员工id")
    @NotNull(message = "员工id不能为空")
    private Integer id;

    @Schema(description = "员工姓名")
    @Size(min = 1, max = 20, message = "员工姓名长度必须在1-20个字符之间")
    private String realName;

    @Schema(description = "员工性别")
    @Min(value = 0, message = "性别值无效")
    @Max(value = 1, message = "性别值无效")
    private Integer gender;

    @Schema(description = "员工手机")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Schema(description = "员工邮箱")
    @Email(message = "邮箱格式不正确")
    private String email;

    @Schema(description = "员工部门id")
    private Integer deptId;

    @Schema(description = "员工职位id")
    private Integer positionId;

    @Schema(description = "员工头像")
    private String avatar;

    @Schema(description = "员工年龄")
    @Min(value = 18, message = "年龄必须大于等于18岁")
    @Max(value = 65, message = "年龄必须小于等于65岁")
    private Integer age;

    @Schema(description = "用户id")
    private Integer userId;

    @Schema(description = "员工住址")
    private String address;

    @Schema(description = "入职时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date hireDate;
}

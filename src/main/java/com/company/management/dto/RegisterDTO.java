package com.company.management.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "用户注册请求参数")
public class RegisterDTO implements Serializable {

    @Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "用户名不能为空")
    @Size(min = 4, max = 16, message = "用户名长度必须在4-16个字符之间")
    private String username;

    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "密码不能为空")
    @Size(min = 4, max = 16, message = "密码长度必须在4-16个字符之间")
    private String password;

    @Schema(description = "真实姓名")
    @Size(max = 20, message = "姓名长度不能超过20个字符")
    private String realName;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "性别：0-女，1-男")
    @Min(value = 0, message = "性别值无效")
    @Max(value = 1, message = "性别值无效")
    private Integer gender;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "部门ID")
    private Integer deptId;

    @Schema(description = "职位ID")
    private Integer positionId;

    @Schema(description = "年龄")
    @Min(value = 18, message = "年龄必须大于等于18岁")
    @Max(value = 65, message = "年龄必须小于等于65岁")
    private Integer age;

    @Schema(description = "住址")
    private String address;
}

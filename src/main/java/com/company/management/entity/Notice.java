package com.company.management.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Schema(description = "公告")
public class Notice {

    @Schema(description = "公告id", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Schema(description = "公告标题")
    @NotBlank(message = "公告标题不能为空")
    private String title;

    @Schema(description = "公告内容")
    private String content;

    @Schema(description = "发布人Id",accessMode = Schema.AccessMode.READ_ONLY)
    private Integer publisherId;

    @Schema(description = "发布人",accessMode = Schema.AccessMode.READ_ONLY)
    private String publisherName;

    @Schema(description = "发布状态")
    @NotNull(message = "发布状态不能为空")
    private Integer status;

    @Schema(description = "创建时间",accessMode = Schema.AccessMode.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @JsonIgnore
    private LocalDateTime updateTime;
}

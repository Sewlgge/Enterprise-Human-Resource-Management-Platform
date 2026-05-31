package com.company.management.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
    /**
     * 用户ID
     */
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 角色
     */
    private Integer role;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 是否删除
     */
    private Integer isDeleted;

    /**
     * 头像（来自关联员工，非 user 表字段）
     */
    private String avatar;
}

package com.company.management.service;

import com.company.management.dto.RegisterDTO;
import com.company.management.entity.Result;

public interface UserService {

    /**
     * 注册
     *
     * @param username
     * @param password
     * @return
     */
    Result register(RegisterDTO registerDTO);

    /**
     * 登录
     *
     * @param username
     * @param password
     * @return
     */
    Result login(String username, String password);

    /**
     * 获取用户信息
     *
     * @return
     */
    Result info();

    /**
     * 登出
     *
     * @return
     */
    Result logout();

    /**
     * 修改密码
     *
     * @param oldPassword
     * @param newPassword
     * @return
     */
    Result update(String oldPassword, String newPassword);
}

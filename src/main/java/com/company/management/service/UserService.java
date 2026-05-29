package com.company.management.service;

import com.company.management.entity.Result;

public interface UserService {

    /**
     * 注册
     *
     * @param username
     * @param password
     * @return
     */
    Result register(String username, String password);

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

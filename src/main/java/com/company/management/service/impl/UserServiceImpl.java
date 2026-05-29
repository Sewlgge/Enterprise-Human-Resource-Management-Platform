package com.company.management.service.impl;

import com.company.management.entity.Result;
import com.company.management.entity.User;
import com.company.management.mapper.UserMapper;
import com.company.management.service.UserService;
import com.company.management.utils.JwtUtil;
import com.company.management.utils.Md5Util;
import com.company.management.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 注册
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    @Transactional
    public Result register(String username, String password) {
        username = username.trim();
        password = password.trim();
        Result validate = validate(username, password);
        if (validate != null) {
            return validate;
        }
        User user = findByUsername(username);
        if (user != null) {
            log.info("用户{}已存在", username);
            return Result.error("用户已存在");
        }
        password = Md5Util.getMD5String(password);
        userMapper.insert(username, password);
        log.info("用户{}注册成功", username);
        return Result.success("注册成功");
    }

    /**
     * 登录
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    @Transactional
    public Result login(String username, String password) {
        username = username.trim();
        password = password.trim();
        Result validate =  validate(username,password);
        if (validate != null) {
            return validate;
        }
        User user = findByUsername(username);
        if (user == null) {
            log.info("用户{}不存在", username);
            return Result.error("用户不存在");
        }
        if (user.getStatus() == 0) {
            log.info("用户{}已禁用", username);
            return Result.error("该用户已被禁用");
        }
        password = Md5Util.getMD5String(password);

        if (!password.equals(user.getPassword())) {
            log.info("用户{}密码错误", username);
            return Result.error("密码错误");
        }

        Map<String,Object> claims = new HashMap<>();
        claims.put("id",user.getId());
        claims.put("username",user.getUsername());
        String token = JwtUtil.genToken(claims);

        String redisKey = "login:token:" + user.getId();
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();

        String oldToken = operations.get(redisKey);
        if (oldToken != null) {
            stringRedisTemplate.delete(oldToken);
            log.info("用户{}旧token已删除", username);
        }

        operations.set(redisKey, token, 1, TimeUnit.HOURS);
        operations.set(token, token, 1, TimeUnit.HOURS);

        log.info("用户{}登录成功", username);
        return Result.success(token);
    }

    @Override
    public Result<User> info() {
        Map<String,Object> map = ThreadLocalUtil.get();
        if (map == null) {
            log.info("用户未登录");
            return Result.error("用户未登录");
        }
        String username = (String)map.get("username");
        User user = findByUsername(username);
        user.setPassword(null);
        log.info("用户{}查询成功", user);
        return Result.success(user);
    }

    @Override
    public Result logout() {
        try {
            Map<String, Object> map = ThreadLocalUtil.get();

            if (map == null || map.isEmpty()) {
                log.warn("用户登出时，ThreadLocal中无数据");
                return Result.error("用户未登录");
            }

            Integer userId = (Integer) map.get("id");
            String token = (String) map.get("token");

            if (userId == null) {
                log.warn("用户登出时，用户ID为空");
                return Result.error("无效的用户信息");
            }

            String redisKey = "login:token:" + userId;

            if (token != null && !token.trim().isEmpty()) {
                stringRedisTemplate.delete(token);
                log.info("用户token已从Redis删除");
            }

            stringRedisTemplate.delete(redisKey);

            String username = (String) map.get("username");
            log.info("用户{}(ID:{})登出成功", username, userId);

            return Result.success("登出成功");
        } catch (Exception e) {
            log.error("用户登出失败", e);
            return Result.error("登出失败，请稍后重试");
        } finally {
            ThreadLocalUtil.remove();
        }
    }

    @Override
    @Transactional
    public Result update(String oldPassword, String newPassword) {
        if (oldPassword == null || oldPassword.trim().isEmpty()) {
            log.info("旧密码不能为空");
            return Result.error("旧密码不能为空");
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            log.info("新密码不能为空");
            return Result.error("新密码不能为空");
        }
        if (oldPassword.length() < 4 || oldPassword.length() > 16) {
            log.info("密码长度必须在4-16个字符之间");
            return Result.error("密码长度必须在4-16个字符之间");
        }
        if (newPassword.length() < 4 || newPassword.length() > 16) {
            log.info("密码长度必须在4-16个字符之间");
            return Result.error("密码长度必须在4-16个字符之间");
        }
        if (oldPassword.equals(newPassword)) {
            log.info("新旧密码不能相同");
            return Result.error("新旧密码不能相同");
        }
        oldPassword = Md5Util.getMD5String(oldPassword.trim());
        Map<String, Object> map = ThreadLocalUtil.get();

        if (map == null || map.isEmpty()) {
            log.warn("用户登出时，ThreadLocal中无数据");
            return Result.error("用户未登录");
        }

        Integer userId = (Integer) map.get("id");

        User user = findByUserId(userId);
        if (user == null) {
            log.info("用户{}不存在", userId);
            return Result.error("用户不存在");
        }
        if (!oldPassword.equals(user.getPassword())) {
            log.info("用户{}密码错误", user.getId());
            return Result.error("密码错误");
        }
        user.setPassword(Md5Util.getMD5String(newPassword.trim()));
        userMapper.update(userId, user.getPassword());
        log.info("用户{}修改密码成功", user.getId());
        return Result.success("修改密码成功");
    }

    private User findByUserId(Integer userId) {
        return userMapper.findByUserId(userId);
    }


    /**
     * 根据用户名查询用户
     *
     * @param username
     * @return
     */
    private User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    /**
     * 验证用户名密码
     *
     * @param username
     * @param password
     * @return
     */
    private Result validate(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            log.info("用户名不能为空");
            return Result.error("用户名不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            log.info("密码不能为空");
            return Result.error("密码不能为空");
        }
        if (username.length() < 4 || username.length() > 16) {
            log.info("用户名长度必须在4-16个字符之间");
            return Result.error("用户名长度必须在4-16个字符之间");
        }
        if (password.length() < 4 || password.length() > 16) {
            log.info("密码长度必须在4-16个字符之间");
            return Result.error("密码长度必须在4-16个字符之间");
        }
        return null;
    }
}

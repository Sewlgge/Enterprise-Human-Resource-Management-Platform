package com.company.management.service.impl;

import com.company.management.dto.RegisterDTO;
import com.company.management.entity.Result;
import com.company.management.entity.User;
import com.company.management.entity.Employee;
import com.company.management.entity.Dept;
import com.company.management.entity.Position;
import com.company.management.mapper.EmployeeMapper;
import com.company.management.mapper.DeptMapper;
import com.company.management.mapper.PositionMapper;
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
    private EmployeeMapper employeeMapper;
    @Autowired
    private DeptMapper deptMapper;
    @Autowired
    private PositionMapper positionMapper;
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
    public Result register(RegisterDTO registerDTO) {
        String username = registerDTO.getUsername().trim();
        String password = registerDTO.getPassword().trim();
        Result validate = validate(username, password);
        if (validate != null) {
            return validate;
        }
        User user = findByUsername(username);
        if (user != null) {
            log.info("用户{}已存在", username);
            return Result.error("用户已存在");
        }

        Result employeeValidate = validateRegisterEmployee(registerDTO);
        if (employeeValidate != null) {
            return employeeValidate;
        }

        Result preCheck = preCheckNewEmployee(registerDTO);
        if (preCheck != null) {
            return preCheck;
        }

        password = Md5Util.getMD5String(password);
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        userMapper.insertUser(newUser);
        if (newUser.getId() == null) {
            throw new IllegalStateException("注册失败，请稍后重试");
        }

        createEmployeeForUser(registerDTO, newUser.getId());

        log.info("用户{}注册成功", username);
        return Result.success("注册成功");
    }

    private Result validateRegisterEmployee(RegisterDTO dto) {
        if (dto.getRealName() == null || dto.getRealName().isBlank()) {
            return Result.error("请输入真实姓名");
        }
        if (dto.getGender() == null) {
            return Result.error("请选择性别");
        }
        if (dto.getPhone() == null || dto.getPhone().isBlank()) {
            return Result.error("请输入手机号");
        }
        if (!dto.getPhone().matches("^1[3-9]\\d{9}$")) {
            return Result.error("手机号格式不正确");
        }
        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            return Result.error("请输入邮箱");
        }
        if (dto.getDeptId() == null) {
            return Result.error("请选择部门");
        }
        if (dto.getPositionId() == null) {
            return Result.error("请选择职位");
        }
        if (dto.getAge() == null) {
            return Result.error("请输入年龄");
        }
        return null;
    }

    private Result preCheckNewEmployee(RegisterDTO dto) {
        Dept dept = deptMapper.getById(dto.getDeptId());
        if (dept == null || dept.getIsDeleted() == 1) {
            return Result.error("部门不存在或已删除");
        }
        Position position = positionMapper.getById(dto.getPositionId());
        if (position == null || position.getIsDeleted() == 1) {
            return Result.error("职位不存在或已删除");
        }
        if (!position.getDeptId().equals(dto.getDeptId())) {
            return Result.error("所选职位不属于该部门");
        }

        Employee existing = employeeMapper.getByNameIncludeDeleted(dto.getRealName().trim());
        if (existing != null && existing.getIsDeleted() == 0) {
            return Result.error("该姓名员工档案已存在");
        }
        return null;
    }

    private void createEmployeeForUser(RegisterDTO dto, Integer userId) {
        Employee employee = new Employee();
        employee.setRealName(dto.getRealName().trim());
        employee.setGender(dto.getGender());
        employee.setPhone(dto.getPhone().trim());
        employee.setEmail(dto.getEmail().trim());
        employee.setDeptId(dto.getDeptId());
        employee.setPositionId(dto.getPositionId());
        employee.setAge(dto.getAge());
        employee.setAddress(dto.getAddress());
        employee.setUserId(userId);

        Boolean added = employeeMapper.add(employee);
        if (added == null || !added) {
            throw new IllegalStateException("创建员工档案失败");
        }
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
        Employee employee = employeeMapper.getByUserId(user.getId());
        if (employee != null) {
            user.setAvatar(employee.getAvatar());
        }
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

        String token = (String) map.get("token");
        if (token != null) {
            stringRedisTemplate.delete(token);
        }
        String redisKey = "login:token:" + userId;
        stringRedisTemplate.delete(redisKey);

        log.info("用户{}修改密码成功", user.getId());
        return Result.success("修改密码成功，请重新登录");
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

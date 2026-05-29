package com.company.management.service.impl;

import com.company.management.dto.*;
import com.company.management.entity.*;
import com.company.management.mapper.DeptMapper;
import com.company.management.mapper.EmployeeMapper;
import com.company.management.mapper.PositionMapper;
import com.company.management.mapper.UserMapper;
import com.company.management.service.EmployeeService;
import com.company.management.utils.OssUtil;
import com.company.management.utils.ThreadLocalUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DeptMapper deptMapper;
    @Autowired
    private PositionMapper positionMapper;
    @Autowired
    private OssUtil ossUtil;

    @Override
    public Result<PageBean<Employee>> page(EmployeePageDTO employeePageDTO) {
        PageBean<Employee> pageBean = new PageBean<>();
        PageHelper.startPage(employeePageDTO.getPageNum(), employeePageDTO.getPageSize());
        List<Employee> employeeList = employeeMapper.list(employeePageDTO);
        //Page中提供了方法，可以获取PageHelper分页查询后    得到的记录和条数
        Map<Integer, String> deptMap = deptMapper.list(new DeptPageDTO()).stream()
                .collect(Collectors.toMap(Dept::getId, Dept::getName));
        employeeList.forEach(employee -> {
            employee.setDeptName(deptMap.get(employee.getDeptId()));
        });
        Map<Integer, String> positionMap = positionMapper.list(new PositionPageDTO()).stream()
                .collect(Collectors.toMap(Position::getId, Position::getName));
        employeeList.forEach(employee -> {
            employee.setPositionName(positionMap.get(employee.getPositionId()));
        });
        Map<Integer, String> userMap = userMapper.list().stream()
                .collect(Collectors.toMap(User::getId, User::getUsername));
        employeeList.forEach(employee -> {
            employee.setUsername(userMap.get(employee.getUserId()));
        });
        Page<Employee> p = (Page<Employee>) employeeList;
        //把数据填充到PageBean对象中
        pageBean.setTotal(p.getTotal());
        pageBean.setItems(p.getResult());
        return Result.success(pageBean);
    }

    @Override
    @Transactional
    public Result add(EmployeeAddDTO employeeAddDTO) {
        // 获取当前登录用户信息
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        User user = userMapper.findByUserId(userId);
        if (user.getRole() != 0) {
            log.info("权限不足");
            return Result.error("权限不足");
        }

        log.info("添加员工: {}", employeeAddDTO);

        // 校验部门ID是否存在
        Dept dept = deptMapper.getById(employeeAddDTO.getDeptId());
        if (dept == null || dept.getIsDeleted() == 1) {
            log.info("部门不存在或已删除: {}", employeeAddDTO.getDeptId());
            return Result.error("部门不存在或已删除");
        }

        // 校验职位ID是否存在
        Position position = positionMapper.getById(employeeAddDTO.getPositionId());
        if (position == null || position.getIsDeleted() == 1) {
            log.info("职位不存在或已删除: {}", employeeAddDTO.getPositionId());
            return Result.error("职位不存在或已删除");
        }

        // 将 DTO 转换为 Entity
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeAddDTO, employee);

        // 查询是否存在同名员工（包含已删除的）
        Employee existingEmployee = employeeMapper.getByNameIncludeDeleted(employee.getRealName());

        if (existingEmployee != null) {
            // 如果存在同名员工，检查是否为软删除状态
            if (existingEmployee.getIsDeleted() == 1) {
                // 恢复已删除的员工并更新信息
                employee.setId(existingEmployee.getId());
                Boolean flag = employeeMapper.restoreAndUpdate(employee);
                if (flag == false) {
                    return Result.error("恢复失败");
                }
                log.info("恢复已删除的员工: {}", existingEmployee.getId());
                return Result.success("添加成功");
            } else {
                // 员工已存在且未删除
                log.info("员工已存在: {}", employee.getRealName());
                return Result.error("员工已存在");
            }
        }

        // 不存在同名员工，直接添加
        Boolean flag = employeeMapper.add(employee);
        if (flag == false) {
            return Result.error("添加失败");
        }
        log.info("添加员工成功");
        return Result.success("添加成功");
    }

    @Override
    public Result<Employee> get(Integer id) {
        Employee employee = employeeMapper.getById(id);
        if (employee == null) {
            return Result.error("员工不存在");
        }
        return Result.success(employee);
    }

    @Override
    @Transactional
    public Result update(EmployeeUpdateDTO employeeUpdateDTO) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        User user = userMapper.findByUserId(userId);
        if (user.getRole() != 0) {
            log.info("权限不足");
            return Result.error("权限不足");
        }

        log.info("开始更新员工信息: {}", employeeUpdateDTO);

        // 查询员工是否存在
        Employee existingEmployee = employeeMapper.getById(employeeUpdateDTO.getId());
        if (existingEmployee == null) {
            log.info("员工不存在: {}", employeeUpdateDTO.getId());
            return Result.error("员工不存在");
        }

        // 如果提供了姓名，检查是否与其他员工重名
        if (employeeUpdateDTO.getRealName() != null && !employeeUpdateDTO.getRealName().equals(existingEmployee.getRealName())) {
            Employee sameNameEmployee = employeeMapper.getByNameIncludeDeleted(employeeUpdateDTO.getRealName());
            if (sameNameEmployee != null && !sameNameEmployee.getId().equals(employeeUpdateDTO.getId())) {
                log.info("员工姓名已存在: {}", employeeUpdateDTO.getRealName());
                return Result.error("员工姓名已存在");
            }
        }

        // 如果提供了部门ID，校验部门是否存在
        if (employeeUpdateDTO.getDeptId() != null) {
            Dept dept = deptMapper.getById(employeeUpdateDTO.getDeptId());
            if (dept == null || dept.getIsDeleted() == 1) {
                log.info("部门不存在或已删除: {}", employeeUpdateDTO.getDeptId());
                return Result.error("部门不存在或已删除");
            }
        }

        // 如果提供了职位ID，校验职位是否存在
        if (employeeUpdateDTO.getPositionId() != null) {
            Position position = positionMapper.getById(employeeUpdateDTO.getPositionId());
            if (position == null || position.getIsDeleted() == 1) {
                log.info("职位不存在或已删除: {}", employeeUpdateDTO.getPositionId());
                return Result.error("职位不存在或已删除");
            }
        }

        Boolean flag = employeeMapper.update(employeeUpdateDTO);
        if (flag == false) {
            return Result.error("更新失败");
        }
        log.info("更新员工成功");
        return Result.success("更新成功");
    }

    @Override
    @Transactional
    public Result delete(Integer id) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        User user = userMapper.findByUserId(userId);
        if (user.getRole() != 0) {
            log.info("权限不足");
            return Result.error("权限不足");
        }
        Boolean flag = employeeMapper.delete(id);
        if (flag == false) {
            log.info("删除失败");
            return Result.error("删除员工失败");
        }
        log.info("删除成功");
        return Result.success("删除成功");
    }

    @Override
    @Transactional
    public Result<String> uploadAvatar(MultipartFile file) {
        // 获取当前登录用户信息
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        User user = userMapper.findByUserId(userId);
        if (user == null) {
            log.info("用户不存在");
            return Result.error("用户不存在");
        }

        // 校验文件是否为空
        if (file == null || file.isEmpty()) {
            return Result.error("上传文件不能为空");
        }

        // 校验文件大小（5MB）
        long maxSize = 5 * 1024 * 1024;
        if (file.getSize() > maxSize) {
            return Result.error("文件大小不能超过5MB");
        }

        // 校验文件类型
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !isImageFile(originalFilename)) {
            return Result.error("只支持 jpg、png、gif、webp 格式的图片");
        }

        // 根据当前用户ID查询员工信息
        Employee employee = employeeMapper.getByUserId(userId);
        if (employee == null) {
            log.info("当前用户未绑定员工信息: userId={}", userId);
            return Result.error("当前用户未绑定员工信息");
        }

        try {
            // 生成对象名称
            String objectName = ossUtil.generateObjectName(employee.getId(), originalFilename);

            // 上传到 OSS
            String avatarUrl = ossUtil.upload(file, objectName);

            // 更新员工头像
            EmployeeUpdateDTO updateDTO = new EmployeeUpdateDTO();
            updateDTO.setId(employee.getId());
            updateDTO.setAvatar(avatarUrl);
            employeeMapper.update(updateDTO);

            log.info("员工 {} 头像上传成功: {}", employee.getId(), avatarUrl);
            return Result.success(avatarUrl);

        } catch (Exception e) {
            log.error("头像上传失败", e);
            return Result.error("头像上传失败: " + e.getMessage());
        }
    }

    /**
     * 判断是否为图片文件
     */
    private boolean isImageFile(String filename) {
        String lowerCase = filename.toLowerCase();
        return lowerCase.endsWith(".jpg") || lowerCase.endsWith(".jpeg") ||
                lowerCase.endsWith(".png") || lowerCase.endsWith(".gif") ||
                lowerCase.endsWith(".webp");
    }
}



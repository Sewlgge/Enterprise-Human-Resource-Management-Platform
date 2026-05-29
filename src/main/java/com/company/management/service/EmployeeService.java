package com.company.management.service;

import com.company.management.dto.EmployeeAddDTO;
import com.company.management.dto.EmployeePageDTO;
import com.company.management.dto.EmployeeUpdateDTO;
import com.company.management.entity.Employee;
import com.company.management.entity.PageBean;
import com.company.management.entity.Result;
import org.springframework.web.multipart.MultipartFile;

public interface EmployeeService {

    /**
     * 分页查询员工列表
     *
     * @param employeePageDTO
     * @return
     */
    Result<PageBean<Employee>> page(EmployeePageDTO employeePageDTO);

    /**
     * 新增员工
     * @param employeeAddDTO
     * @return
     */
    Result add(EmployeeAddDTO employeeAddDTO);

    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    Result<Employee> get(Integer id);

    /**
     * 修改员工
     * @param employeeUpdateDTO
     * @return
     */
    Result update(EmployeeUpdateDTO employeeUpdateDTO);

    /**
     * 删除员工
     * @param id
     * @return
     */
    Result delete(Integer id);

    /**
     * 上传员工头像（当前登录用户）
     * @param file 头像文件
     * @return 头像 URL
     */
    Result<String> uploadAvatar(MultipartFile file);
}

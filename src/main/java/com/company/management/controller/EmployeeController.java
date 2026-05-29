package com.company.management.controller;

import com.company.management.dto.EmployeeAddDTO;
import com.company.management.dto.EmployeePageDTO;
import com.company.management.dto.EmployeeUpdateDTO;
import com.company.management.entity.Employee;
import com.company.management.entity.PageBean;
import com.company.management.entity.Result;
import com.company.management.service.EmployeeService;
import com.company.management.utils.OssUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/employee")
@Validated
@Tag(name = "员工管理")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @RequestMapping("/page")
    @Operation(summary = "员工分页查询")
    public Result<PageBean<Employee>> page(EmployeePageDTO employeePageDTO) {
        return employeeService.page(employeePageDTO);
    }

    @PostMapping("/add")
    @Operation(summary = "员工添加")
    public Result add(@Validated @RequestBody EmployeeAddDTO employeeAddDTO) {
        return employeeService.add(employeeAddDTO);
    }

    @GetMapping("get/{id}")
    @Operation(summary = "员工详情")
    public Result<Employee> get(@PathVariable("id") Integer id) {
        return employeeService.get(id);
    }

    @PutMapping("/update")
    @Operation(summary = "员工更新")
    public Result update(@Validated @RequestBody EmployeeUpdateDTO employeeUpdateDTO) {
        return employeeService.update(employeeUpdateDTO);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "员工删除")
    public Result delete(@PathVariable("id") Integer id) {
        return employeeService.delete(id);
    }

    @PostMapping("/upload/avatar")
    @Operation(summary = "上传员工头像")
    public Result<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        return employeeService.uploadAvatar(file);
    }

/*    @PostMapping("/fix/avatar/metadata")
    @Operation(summary = "修复头像元数据（仅用于测试）", deprecated = true)
    public Result fixAvatarMetadata(@RequestParam("objectName") String objectName) {
        try {
            ossUtil.updateObjectMetadata(objectName);
            return Result.success("元数据更新成功，现在可以预览了");
        } catch (Exception e) {
            return Result.error("更新失败: " + e.getMessage());
        }
    }*/

}

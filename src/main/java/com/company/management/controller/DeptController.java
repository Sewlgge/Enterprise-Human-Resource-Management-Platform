package com.company.management.controller;

import com.company.management.dto.DeptPageDTO;
import com.company.management.entity.Dept;
import com.company.management.entity.PageBean;
import com.company.management.entity.Result;
import com.company.management.service.DeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dept")
@Validated
@Tag(name = "部门管理")
public class DeptController {
    @Autowired
    private DeptService deptService;

    @GetMapping("/page")
    @Operation(summary = "分页查询部门列表")
    public Result<PageBean<Dept>> page(DeptPageDTO deptPageDTO) {
        return deptService.page(deptPageDTO);
    }

    @GetMapping("/list")
    @Operation(summary = "查询所有部门")
    public Result<List<Dept>> list(@Parameter(description = "部门名称")
                                 @RequestParam(required = false, value = "name") String name ) {
        return deptService.list(name);
    }

    @PostMapping("/add")
    @Operation(summary = "添加部门")
    public Result add(@Parameter(description = "部门名称") @RequestParam("name") String name,
                            @Parameter(description = "部门描述") @RequestParam("description") String description){
        return deptService.add(name,description);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除部门")
    public Result delete(@Parameter(description = "部门ID") @PathVariable("id") Integer id){
        return deptService.delete(id);
    }

    @PutMapping("/update")
    @Operation(summary = "修改部门")
    public Result update(@Parameter(description = "部门ID") @RequestParam("id")Integer id,
                         @Parameter(description = "部门名称") @RequestParam("name") String name,
                         @Parameter(description = "部门描述") @RequestParam("description") String description) {
        return deptService.update(id,name,description);
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "根据id查询部门")
    public Result<Dept> getById(@Parameter(description = "部门ID") @PathVariable("id") Integer id) {
        return deptService.getById(id);
    }
}

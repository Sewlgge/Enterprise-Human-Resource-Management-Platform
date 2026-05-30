package com.company.management.controller;

import com.company.management.annotation.OperateLog;
import com.company.management.dto.PositionAddDTO;
import com.company.management.dto.PositionPageDTO;
import com.company.management.dto.PositionUpdateDTO;
import com.company.management.entity.PageBean;
import com.company.management.entity.Position;
import com.company.management.entity.Result;
import com.company.management.service.PositionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/position")
@Validated
@Tag(name = "职位管理")
public class PositionController {
    @Autowired
    private PositionService positionservice;

    @GetMapping("/page")
    @OperateLog("职位分页查询")
    @Operation(summary = "职位分页查询")
    public Result<PageBean<Position>> page(PositionPageDTO positionPageDTO) {
        return positionservice.page(positionPageDTO);
    }

    @GetMapping("/list")
    @OperateLog("职位列表查询")
    @Operation(summary = "职位列表查询")
    public Result<List<Position>> list(@Parameter(description = "职位名称") @RequestParam(name = "name",required = false) String name,
                                       @Parameter(description = "所属部门id") @RequestParam(name = "deptId",required = false) Integer deptId) {
        return positionservice.list(name, deptId);
    }

    @GetMapping("/{id}")
    @OperateLog("根据id查询职位")
    @Operation(summary = "根据id查询职位")
    public Result<Position> getById(@Parameter(description = "职位id") @PathVariable("id") Integer id) {
        return positionservice.getById(id);
    }

    @PostMapping
    @OperateLog("添加职位")
    @Operation(summary = "添加职位")
    public Result<String> add(@RequestBody @Validated PositionAddDTO positionAddDTO) {
        return positionservice.add(positionAddDTO);
    }

    @PutMapping("update")
    @OperateLog("修改职位信息")
    @Operation(summary = "修改职位信息")
    public Result<String> update(@RequestBody @Validated PositionUpdateDTO positionUpdateDTO) {
        return positionservice.update(positionUpdateDTO);
    }

    @DeleteMapping("delete/{id}")
    @OperateLog("删除职位")
    @Operation(summary = "删除职位")
    public Result<String> delete(@Parameter(description = "职位id") @PathVariable("id") Integer id) {
        return positionservice.delete(id);
    }

}

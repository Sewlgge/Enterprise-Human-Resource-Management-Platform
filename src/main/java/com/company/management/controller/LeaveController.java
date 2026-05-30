package com.company.management.controller;

import com.company.management.dto.LeaveDTO;
import com.company.management.dto.LeavePageDTO;
import com.company.management.entity.LeaveRequest;
import com.company.management.entity.PageBean;
import com.company.management.entity.Result;
import com.company.management.service.LeaveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leave")
@Validated
@Tag(name = "请假模块")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    @PostMapping
    @Operation(summary = "请假申请")
    public Result leave(@Validated LeaveDTO leaveDTO) {
        return leaveService.leave(leaveDTO);
    }

    @GetMapping("my")
    @Operation(summary = "我的请假记录")
    public Result<List<LeaveRequest>> myLeave() {
        return leaveService.myLeave();
    }

    @GetMapping("/{id}")
    @Operation(summary = "请假记录详情")
    public Result<LeaveRequest> leaveDetail(@PathVariable("id") Integer id) {
        return leaveService.leaveDetail(id);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询请假记录")
    public Result<PageBean<LeaveRequest>> leavePage(LeavePageDTO leavePageDTO) {
        return leaveService.leavePage(leavePageDTO);
    }

    @PutMapping("/approver/{id}")
    @Operation(summary = "审批考勤")
    public Result<String> approver(@PathVariable("id") Integer id,
                                   @Schema(description = "审批状态") @RequestParam(name = "status") Integer status,
                                   @Schema(description = "审批备注") @RequestParam(name = "remark",required = false) String remark) {
        return leaveService.approver(id, status, remark);
    }


}

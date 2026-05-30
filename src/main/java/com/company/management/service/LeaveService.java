package com.company.management.service;

import com.company.management.dto.LeaveDTO;
import com.company.management.dto.LeavePageDTO;
import com.company.management.entity.LeaveRequest;
import com.company.management.entity.PageBean;
import com.company.management.entity.Result;

import java.util.List;

public interface LeaveService {

    /**
     * 请假申请
     * @param leaveDTO 请假参数
     * @return 请假结果
     */
    Result leave(LeaveDTO leaveDTO);

    /**
     * 我的请假
     * @return 我的请假结果
     */
    Result<List<LeaveRequest>> myLeave();

    /**
     * 请假详情
     * @param id 请假id
     * @return 请假详情结果
     */
    Result<LeaveRequest> leaveDetail(Integer id);

    /**
     * 请假分页查询
     * @return 请假分页结果
     */
    Result<PageBean<LeaveRequest>> leavePage(LeavePageDTO leavePageDTO);

    /**
     * 审批请假
     * @param id 请假id
     * @param status 审批状态
     * @param remark 审批备注
     * @return 审批结果
     */
    Result<String> approver(Integer id, Integer status, String remark);
}

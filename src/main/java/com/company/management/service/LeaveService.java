package com.company.management.service;

import com.company.management.dto.LeaveDTO;
import com.company.management.entity.LeaveRequest;
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
}

package com.company.management.service;

import com.company.management.dto.LeaveDTO;
import com.company.management.entity.Result;

public interface LeaveService {

    /**
     * 请假申请
     * @param leaveDTO 请假参数
     * @return 请假结果
     */
    Result leave(LeaveDTO leaveDTO);
}

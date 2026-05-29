package com.company.management.service.impl;

import com.company.management.dto.LeaveDTO;
import com.company.management.entity.Employee;
import com.company.management.entity.LeaveRequest;
import com.company.management.entity.Result;
import com.company.management.mapper.EmployeeMapper;
import com.company.management.mapper.LeaveMapper;
import com.company.management.service.LeaveService;
import com.company.management.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class LeaveServiceImpl implements LeaveService {
    @Autowired
    private LeaveMapper leaveMapper;
    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    @Transactional
    public Result leave(LeaveDTO leaveDTO) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        Employee employee = employeeMapper.getByUserId(userId);
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setEmployeeId(employee.getId());
        leaveRequest.setStartDate(leaveDTO.getStartDate());
        leaveRequest.setDays(leaveDTO.getDays());
        leaveRequest.setEndDate(leaveDTO.getStartDate().plusDays(leaveDTO.getDays()));
        leaveRequest.setType(leaveDTO.getType());
        leaveRequest.setReason(leaveDTO.getReason());
        boolean result = leaveMapper.add(leaveRequest);
        if (result == false) {
            return Result.error("申请失败");
        }
        return Result.success("申请成功");
    }

    @Override
    public Result<List<LeaveRequest>> myLeave() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        Employee employee = employeeMapper.getByUserId(userId);
        List<LeaveRequest> list = leaveMapper.list(employee.getId());
        return Result.success(list);
    }

    @Override
    public Result<LeaveRequest> leaveDetail(Integer id) {
        LeaveRequest leaveRequest = leaveMapper.getById(id);
        return Result.success(leaveRequest);
    }
}

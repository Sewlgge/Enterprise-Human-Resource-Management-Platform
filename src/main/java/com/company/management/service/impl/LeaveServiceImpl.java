package com.company.management.service.impl;

import com.company.management.dto.EmployeePageDTO;
import com.company.management.dto.LeaveDTO;
import com.company.management.dto.LeavePageDTO;
import com.company.management.entity.*;
import com.company.management.mapper.EmployeeMapper;
import com.company.management.mapper.LeaveMapper;
import com.company.management.mapper.UserMapper;
import com.company.management.service.LeaveService;
import com.company.management.utils.ThreadLocalUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LeaveServiceImpl implements LeaveService {
    private static final Logger log = LoggerFactory.getLogger(LeaveServiceImpl.class);
    @Autowired
    private LeaveMapper leaveMapper;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private UserMapper userMapper;

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

    @Override
    public Result<PageBean<LeaveRequest>> leavePage(LeavePageDTO leavePageDTO) {
        PageBean<LeaveRequest> pageBean = new PageBean<>();
        PageHelper.startPage(leavePageDTO.getPageNum(), leavePageDTO.getPageSize());
        List<LeaveRequest> leaveList = leaveMapper.page(leavePageDTO);
        log.info("请假列表：{}", leaveList);
        Map<Integer, String> employeeMap = employeeMapper.list(new EmployeePageDTO())
                .stream().collect(Collectors.toMap(Employee::getId, Employee::getRealName));
        leaveList.forEach(leaveRequest -> leaveRequest.setEmployeeName(employeeMap.get(leaveRequest.getEmployeeId())));
        log.info("员工列表：{}", employeeMap);
        leaveList.forEach(leaveRequest -> leaveRequest.setApproverName(employeeMap.get(leaveRequest.getApproverId())));
        Page<LeaveRequest> p = (Page<LeaveRequest>) leaveList;
        //把数据填充到PageBean对象中
        pageBean.setTotal(p.getTotal());
        pageBean.setItems(p.getResult());
        return Result.success(pageBean);
    }

    @Override
    @Transactional
    public Result<String> approver(Integer id, Integer status, String remark) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        User user = userMapper.findByUserId(userId);
        if (user.getRole() != 0) {
            log.info("权限不足");
            return Result.error("权限不足");
        }
        Employee employee = employeeMapper.getByUserId(userId);
        int result = leaveMapper.update(id, status, remark, employee.getId());
        if (result == 0) {
            log.info("操作失败");
            return Result.error("操作失败");
        }
        return Result.success("操作成功");
    }
}

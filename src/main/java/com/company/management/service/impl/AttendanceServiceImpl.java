package com.company.management.service.impl;

import com.company.management.dto.AttendancePageDTO;
import com.company.management.dto.AttendanceUpdateDTO;
import com.company.management.dto.EmployeePageDTO;
import com.company.management.entity.*;
import com.company.management.mapper.AttendanceMapper;
import com.company.management.mapper.EmployeeMapper;
import com.company.management.mapper.UserMapper;
import com.company.management.service.AttendanceService;
import com.company.management.utils.ThreadLocalUtil;
import com.company.management.vo.AttendanceVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AttendanceServiceImpl implements AttendanceService {

    private static final LocalTime SIGN_IN_DEADLINE = LocalTime.of(9, 0);
    private static final LocalTime SIGN_OUT_DEADLINE = LocalTime.of(18, 0);

    @Autowired
    private AttendanceMapper attendanceMapper;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public Result siginIn() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");

        Employee employee = employeeMapper.getByUserId(userId);
        if (employee == null) {
            return Result.error("当前用户未绑定员工信息");
        }

        LocalDate today = LocalDate.now();
        Integer attendanceId = attendanceMapper.getTodayByEmployeeId(employee.getId(), today);
        if (attendanceId == null) {
            List<Integer> empIds = new ArrayList<>();
            empIds.add(employee.getId());
            attendanceMapper.batchInsert(empIds, today);
        }

        LocalDateTime now = LocalDateTime.now();
        int signInStatus = now.toLocalTime().isBefore(SIGN_IN_DEADLINE) ? 0 : 1;
        int rows = attendanceMapper.signIn(employee.getId(), now, signInStatus);
        if (rows == 0) {
            return Result.error("今日已签到，无需重复签到");
        }

        String statusText = signInStatus == 0 ? "正常" : "迟到";
        log.info("员工[{}]签到成功，时间：{}，状态：{}", employee.getRealName(), now, statusText);
        return Result.success("签到成功（" + statusText + "）");
    }

    @Override
    @Transactional
    public void initDailyAttendance() {
        LocalDate today = LocalDate.now();
        log.info("开始初始化今日考勤表，日期：{}", today);

        List<Integer> allEmployeeIds = attendanceMapper.getAllActiveEmployeeIds();
        if (allEmployeeIds.isEmpty()) {
            log.info("无在职员工，跳过考勤初始化");
            return;
        }

        List<Integer> recordedIds = attendanceMapper.getTodayRecordedEmployeeIds(today);
        Set<Integer> recordedSet = recordedIds.stream().collect(Collectors.toSet());

        List<Integer> unrecordedIds = new ArrayList<>();
        for (Integer empId : allEmployeeIds) {
            if (!recordedSet.contains(empId)) {
                unrecordedIds.add(empId);
            }
        }

        if (!unrecordedIds.isEmpty()) {
            int rows = attendanceMapper.batchInsert(unrecordedIds, today);
            log.info("初始化考勤表完成，新增 {} 条记录", rows);
        } else {
            log.info("今日考勤表已完整，无需初始化");
        }
    }

    @Override
    @Transactional
    public Result siginOut() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");

        Employee employee = employeeMapper.getByUserId(userId);
        if (employee == null) {
            return Result.error("当前用户未绑定员工信息");
        }

        LocalDate today = LocalDate.now();
        Integer attendanceId = attendanceMapper.getTodayByEmployeeId(employee.getId(), today);
        if (attendanceId == null) {
            List<Integer> empIds = new ArrayList<>();
            empIds.add(employee.getId());
            attendanceMapper.batchInsert(empIds, today);
        }

        LocalDateTime now = LocalDateTime.now();
        int signOutStatus = now.toLocalTime().isAfter(SIGN_OUT_DEADLINE) ? 0 : 2;
        int rows = attendanceMapper.signOut(employee.getId(), now, signOutStatus);
        if (rows == 0) {
            return Result.error("今日已签退，无需重复签退");
        }

        String statusText = signOutStatus == 0 ? "正常" : "早退";
        log.info("员工[{}]签退成功，时间：{}，状态：{}", employee.getRealName(), now, statusText);
        return Result.success("签退成功（" + statusText + "）");
    }

    @Override
    @Transactional
    public void updatePreviousDayStatus() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        log.info("开始更新前一天[{}]的打卡状态", yesterday);
        int rows = attendanceMapper.updatePreviousDayStatus(yesterday);
        log.info("前一天打卡状态更新完成，影响 {} 条记录", rows);
    }

    @Override
    public Result<List<Attendance>> getMyAttendance(Integer month) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");

        Employee employee = employeeMapper.getByUserId(userId);
        if (employee == null) {
            return Result.error("当前用户未绑定员工信息");
        }
        List<Attendance> list = attendanceMapper.getByEmployeeId(employee.getId(),month);
        if (list.isEmpty()) {
            return Result.success(new ArrayList<>());
        }
        return Result.success(list);
    }

    @Override
    public Result<PageBean<AttendanceVO>> page(AttendancePageDTO attendancePageDTO) {
        PageBean<AttendanceVO> pageBean = new PageBean<>();
        PageHelper.startPage(attendancePageDTO.getPage(), attendancePageDTO.getSize());
        List<AttendanceVO> attendanceList = attendanceMapper.page(attendancePageDTO);
        Map<Integer,String> employeeMap = employeeMapper.list(new EmployeePageDTO()).stream()
                .collect(Collectors.toMap(Employee::getId, Employee::getRealName));
        attendanceList.forEach(attendance -> {
            attendance.setEmployeeName(employeeMap.get(attendance.getEmployeeId()));
        });
        Page<AttendanceVO> p = (Page<AttendanceVO>) attendanceList;
        //把数据填充到PageBean对象中
        pageBean.setTotal(p.getTotal());
        pageBean.setItems(p.getResult());
        return Result.success(pageBean);
    }

    @Override
    public Result<String> update(AttendanceUpdateDTO attendance) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        User user = userMapper.findByUserId(userId);
        if (user.getRole() != 0) {
            log.info("权限不足");
            return Result.error("权限不足");
        }
        int rows = attendanceMapper.update(attendance);
        if (rows == 0) {
            return Result.error("更新失败");
        }
        return Result.success("更新成功");
    }
}

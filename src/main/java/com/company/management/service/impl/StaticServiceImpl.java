package com.company.management.service.impl;

import com.company.management.entity.Result;
import com.company.management.entity.Static;
import com.company.management.entity.User;
import com.company.management.mapper.StaticMapper;
import com.company.management.mapper.UserMapper;
import com.company.management.service.StaticService;
import com.company.management.utils.ThreadLocalUtil;
import com.company.management.vo.AttendanceStatusSummaryVO;
import com.company.management.vo.DashboardVO;
import com.company.management.vo.DeptEmployeeCountVO;
import com.company.management.vo.LeaveStatusSummaryVO;
import com.company.management.vo.MonthAttendanceVO;
import com.company.management.vo.MonthLeaveVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StaticServiceImpl implements StaticService {

    @Autowired
    private StaticMapper staticMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Result<Static> staticborad() {
        Result<Void> auth = checkAdmin();
        if (auth != null) {
            return Result.error(auth.getMessage());
        }
        return Result.success(staticMapper.staticborad());
    }

    @Override
    public Result<DashboardVO> dashboard() {
        Result<Void> auth = checkAdmin();
        if (auth != null) {
            return Result.error(auth.getMessage());
        }
        DashboardVO dashboard = new DashboardVO();
        dashboard.setSummary(staticMapper.staticborad());
        dashboard.setMonthAttendance(buildMonthAttendance());
        dashboard.setDeptEmployeeCounts(staticMapper.listDeptEmployeeCount());
        dashboard.setMonthLeave(buildMonthLeave());
        return Result.success(dashboard);
    }

    @Override
    public Result<MonthAttendanceVO> monthAttendance() {
        Result<Void> auth = checkAdmin();
        if (auth != null) {
            return Result.error(auth.getMessage());
        }
        return Result.success(buildMonthAttendance());
    }

    @Override
    public Result<List<DeptEmployeeCountVO>> deptEmployeeCount() {
        Result<Void> auth = checkAdmin();
        if (auth != null) {
            return Result.error(auth.getMessage());
        }
        return Result.success(staticMapper.listDeptEmployeeCount());
    }

    @Override
    public Result<MonthLeaveVO> monthLeave() {
        Result<Void> auth = checkAdmin();
        if (auth != null) {
            return Result.error(auth.getMessage());
        }
        return Result.success(buildMonthLeave());
    }

    private MonthAttendanceVO buildMonthAttendance() {
        MonthAttendanceVO vo = new MonthAttendanceVO();
        vo.setSummary(defaultAttendanceSummary(staticMapper.countMonthAttendanceStatus()));
        vo.setDailyStats(staticMapper.listMonthDailyAttendanceStats());
        vo.setRecords(staticMapper.listMonthAttendanceRecords());
        return vo;
    }

    private MonthLeaveVO buildMonthLeave() {
        MonthLeaveVO vo = new MonthLeaveVO();
        vo.setSummary(defaultLeaveSummary(staticMapper.countMonthLeaveStatus()));
        vo.setTypeStats(staticMapper.listMonthLeaveByType());
        vo.setRecords(staticMapper.listMonthLeaveRecords());
        return vo;
    }

    private AttendanceStatusSummaryVO defaultAttendanceSummary(AttendanceStatusSummaryVO summary) {
        if (summary != null) {
            return summary;
        }
        summary = new AttendanceStatusSummaryVO();
        summary.setTotal(0);
        summary.setNormal(0);
        summary.setLate(0);
        summary.setEarlyLeave(0);
        summary.setMissing(0);
        summary.setLeaveCount(0);
        return summary;
    }

    private LeaveStatusSummaryVO defaultLeaveSummary(LeaveStatusSummaryVO summary) {
        if (summary != null) {
            return summary;
        }
        summary = new LeaveStatusSummaryVO();
        summary.setTotal(0);
        summary.setPending(0);
        summary.setApproved(0);
        summary.setRejected(0);
        summary.setTotalDays(0);
        return summary;
    }

    private Result<Void> checkAdmin() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        User user = userMapper.findByUserId(userId);
        if (user.getRole() != 0) {
            log.info("权限不足");
            return Result.error("权限不足");
        }
        return null;
    }
}

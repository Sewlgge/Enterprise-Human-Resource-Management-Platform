package com.company.management.service;

import com.company.management.entity.Result;
import com.company.management.entity.Static;
import com.company.management.vo.DashboardVO;
import com.company.management.vo.DeptEmployeeCountVO;
import com.company.management.vo.MonthAttendanceVO;
import com.company.management.vo.MonthLeaveVO;

import java.util.List;

public interface StaticService {

    Result<Static> staticborad();

    Result<DashboardVO> dashboard();

    Result<MonthAttendanceVO> monthAttendance();

    Result<List<DeptEmployeeCountVO>> deptEmployeeCount();

    Result<MonthLeaveVO> monthLeave();
}

package com.company.management.mapper;

import com.company.management.entity.LeaveRequest;
import com.company.management.entity.Static;
import com.company.management.vo.AttendanceRecordVO;
import com.company.management.vo.AttendanceStatusSummaryVO;
import com.company.management.vo.DailyAttendanceStatVO;
import com.company.management.vo.DeptEmployeeCountVO;
import com.company.management.vo.LeaveStatusSummaryVO;
import com.company.management.vo.LeaveTypeStatVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StaticMapper {

    Static staticborad();

    AttendanceStatusSummaryVO countMonthAttendanceStatus();

    List<DailyAttendanceStatVO> listMonthDailyAttendanceStats();

    List<AttendanceRecordVO> listMonthAttendanceRecords();

    List<DeptEmployeeCountVO> listDeptEmployeeCount();

    LeaveStatusSummaryVO countMonthLeaveStatus();

    List<LeaveTypeStatVO> listMonthLeaveByType();

    List<LeaveRequest> listMonthLeaveRecords();
}

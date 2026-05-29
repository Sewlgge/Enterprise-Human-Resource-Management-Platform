package com.company.management.service;

import com.company.management.dto.AttendancePageDTO;
import com.company.management.dto.AttendanceUpdateDTO;
import com.company.management.entity.Attendance;
import com.company.management.entity.PageBean;
import com.company.management.entity.Result;
import com.company.management.vo.AttendanceVO;

import java.util.List;

public interface AttendanceService {

    /**
     * 签到
     */
    Result siginIn();

    /**
     * 初始化每日考勤表：为所有在职员工生成当天的考勤记录（如果不存在）
     */
    void initDailyAttendance();

    /**
     * 签退
     */
    Result siginOut();

    /**
     * 更新前一天的打卡状态（未签到→缺勤，已签到未签退→异常）
     */
    void updatePreviousDayStatus();

    /**
     * 获取当前登录用户的考勤记录
     */
    Result<List<Attendance>> getMyAttendance(Integer day);

    /**
     * 分页查询考勤记录
     */
    Result<PageBean<AttendanceVO>> page(AttendancePageDTO attendancePageDTO);

    /**
     * 修改考勤记录
     */
    Result<String> update(AttendanceUpdateDTO attendance);
}

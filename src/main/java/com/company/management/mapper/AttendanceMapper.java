



























package com.company.management.mapper;

import com.company.management.dto.AttendancePageDTO;
import com.company.management.dto.AttendanceUpdateDTO;
import com.company.management.entity.Attendance;
import com.company.management.vo.AttendanceVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface AttendanceMapper {

    /**
     * 查询今日考勤记录（通过员工ID）
     */
    Integer getTodayByEmployeeId(@Param("employeeId") Integer employeeId, @Param("today") LocalDate today);

    /**
     * 签到：更新签到时间和状态（0=正常 1=迟到）
     */
    int signIn(@Param("employeeId") Integer employeeId,
               @Param("signInTime") LocalDateTime signInTime,
               @Param("status") Integer status);

    /**
     * 批量插入考勤记录（初始化每日考勤表）
     */
    int batchInsert(@Param("employeeIds") List<Integer> employeeIds, @Param("today") LocalDate today);

    /**
     * 查询所有在职员工的ID
     */
    List<Integer> getAllActiveEmployeeIds();

    /**
     * 查询今天已有考勤记录的员工ID列表
     */
    List<Integer> getTodayRecordedEmployeeIds(@Param("today") LocalDate today);

    /**
     * 签退：更新签退时间和状态（0=正常 2=早退）
     **/
    int signOut(@Param("employeeId") Integer employeeId,
                @Param("signOutTime") LocalDateTime now,
                @Param("status") Integer status);

    /**
     * 更新前一天的打卡状态（sign_in_time 为空 → 缺卡 3）
     */
    int updatePreviousDayStatus(@Param("yesterday") LocalDate yesterday);

    /**
     * 查询指定员工的考勤记录
     */
    List<Attendance> getByEmployeeId(@Param("employeeId") Integer employeeId,@Param("month") Integer month);

    /**
     * 分页查询
     */
    List<AttendanceVO> page(@Param("dto") AttendancePageDTO attendancePageDTO);

    /**
     * 修改考勤记录
     */
    int update(AttendanceUpdateDTO attendance);
}

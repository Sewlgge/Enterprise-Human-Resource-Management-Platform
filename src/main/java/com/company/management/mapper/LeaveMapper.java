package com.company.management.mapper;

import com.company.management.entity.LeaveRequest;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LeaveMapper {

    /**
     * 添加请假申请
     * @param leaveRequest 请假申请
     * @return 是否添加成功
     */
    @Insert("insert into leave_request(employee_id,start_date,end_date,days,`type`,reason)" +
            "values (#{employeeId},#{startDate},#{endDate},#{days},#{type},#{reason})")
    boolean add(LeaveRequest leaveRequest);
}

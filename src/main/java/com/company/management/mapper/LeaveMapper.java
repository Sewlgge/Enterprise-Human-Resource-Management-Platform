package com.company.management.mapper;

import com.company.management.entity.LeaveRequest;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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

    /**
     * 获取请假申请列表
     * @param id 员工id
     * @return 请假申请列表
     */
    @Select("select * from leave_request where employee_id = #{employeeid} order by apply_time desc")
    List<LeaveRequest> list(Integer employeeid);
}

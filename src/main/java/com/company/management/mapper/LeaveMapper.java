package com.company.management.mapper;

import com.company.management.dto.LeavePageDTO;
import com.company.management.entity.LeaveRequest;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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
     * @param employeeid 员工id
     * @return 请假申请列表
     */
    @Select("select * from leave_request where employee_id = #{employeeid} order by apply_time desc")
    List<LeaveRequest> list(Integer employeeid);

    /**
     * 获取请假申请
     *
     * @param id 请假申请id
     * @return 请假申请
     */
    @Select("select * from leave_request where id = #{id}")
    LeaveRequest getById(Integer id);

    /**
     * 获取请假申请列表
     *
     * @param leavePageDTO 请假申请列表参数
     * @return 请假申请列表
     */
    List<LeaveRequest> page(@Param("dto") LeavePageDTO leavePageDTO);

    /**
     * 审批请假申请
     *
     * @param id         请假申请id
     * @param status     请假申请状态
     * @param remark     请假申请备注
     * @param employeeId
     * @return 是否更新成功
     */
    int update(@Param("id") Integer id, @Param("status") Integer status, @Param("remark") String remark, @Param("employeeId") Integer employeeId);
}

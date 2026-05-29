package com.company.management.mapper;

import com.company.management.dto.EmployeePageDTO;
import com.company.management.dto.EmployeeUpdateDTO;
import com.company.management.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    /**
     * 查询员工列表
     * @param employeePageDTO
     * @return
     */
    List<Employee> list(@Param("dto") EmployeePageDTO employeePageDTO);

    /**
     * 根据姓名查询员工（包含已删除的）
     * @param realName
     * @return
     */
    @Select("select * from employee where real_name = #{realName} limit 1")
    Employee getByNameIncludeDeleted(String realName);

    /**
     * 添加员工
     * @param employee
     * @return
     */
    Boolean add(@Param("dto") Employee employee);

    /**
     * 恢复已删除的员工并更新信息
     * @param employee
     * @return
     */
    @Update("update employee set real_name = #{dto.realName}, gender = #{dto.gender}, phone = #{dto.phone}, email = #{dto.email}, dept_id = #{dto.deptId}, position_id = #{dto.positionId}, avatar = #{dto.avatar}, age = #{dto.age}, user_id = #{dto.userId}, address = #{dto.address}, hire_date = #{dto.hireDate}, is_deleted = 0, update_time = now() where id = #{dto.id}")
    Boolean restoreAndUpdate(@Param("dto") Employee employee);

    /**
     * 根据ID查询员工信息
     * @param id
     * @return
     */
    @Select("select * from employee where id = #{id} and is_deleted = 0")
    Employee getById(Integer id);

    /**
     * 根据用户ID查询员工信息
     * @param userId
     * @return
     */
    @Select("select * from employee where user_id = #{userId} and is_deleted = 0 limit 1")
    Employee getByUserId(Integer userId);

    /**
     * 根据id 更新员工信息
     * @param employeeUpdateDTO
     * @return
     */
    Boolean update(@Param("dto") EmployeeUpdateDTO employeeUpdateDTO);

    /**
     * 根据id 删除员工信息
     * @param id
     * @return
     */
    @Update("update employee set is_deleted = 1 where id = #{id}")
    Boolean delete(Integer id);
}

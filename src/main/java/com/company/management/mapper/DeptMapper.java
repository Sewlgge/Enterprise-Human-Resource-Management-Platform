package com.company.management.mapper;

import com.company.management.dto.DeptPageDTO;
import com.company.management.entity.Dept;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DeptMapper {

    List<Dept> list(@Param("dto") DeptPageDTO deptPageDTO);

    @Insert("insert into dept(name,description) values(#{name},#{description})")
    Boolean add(@Param("name") String name, @Param("description") String description);

    @Update("update dept set is_deleted = 1 where id=#{id}")
    Boolean delete(@Param("id") Integer id);

    @Select("select * from dept where name=#{name} and is_deleted = 0")
    Dept getDeptByName(String name);

    @Select("select * from dept where name=#{name} limit 1")
    Dept getDeptByNameIncludeDeleted(String name);

    @Update("update dept set name=#{dto.name},description=#{dto.description},is_deleted = #{dto.isDeleted} where id=#{dto.id}")
    Boolean updateDept(@Param("dto") Dept dept);

    @Select("select * from dept where id= #{id}")
    Dept getById(Integer id);
}

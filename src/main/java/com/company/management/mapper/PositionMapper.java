package com.company.management.mapper;

import com.company.management.dto.PositionPageDTO;
import com.company.management.dto.PositionUpdateDTO;
import com.company.management.entity.Position;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface PositionMapper {

    /**
     * 分页查询职位列表
     * @param positionPageDTO
     * @return
     */
    List<Position> list(@Param("dto") PositionPageDTO positionPageDTO);

    /**
     * 根据id查询职位
     * @param id
     * @return
     */
    @Select("select * from position where id = #{id} and is_deleted = 0")
    Position getById(Integer id);

    /**
     * 根据名称查询职位（包含已删除的）
     * @param name
     * @return
     */
    @Select("select * from position where name = #{name} limit 1")
    Position getByNameIncludeDeleted(String name);

    /**
     * 添加职位
     *
     * @param position
     * @return
     */
    Boolean add(@Param("dto") Position position);

    /**
     * 修改职位
     *
     * @param positionAddDTO
     * @return
     */
    @Update("update position set name = #{dto.name}, description = #{dto.description}, update_time = now() where id = #{dto.id}")
    Boolean update(@Param("dto") PositionUpdateDTO positionAddDTO);

    /**
     * 删除职位
     *
     * @param id
     * @return
     */
    @Update("update position set is_deleted = 1 where id = #{id}")
    Boolean delete(Integer id);

    /**
     * 恢复已删除的职位并更新信息
     *
     * @param position
     * @return
     */
    @Update("update position set name = #{dto.name}, description = #{dto.description}, dept_id = #{dto.deptId}, is_deleted = 0, update_time = now() where id = #{dto.id}")
    Boolean restoreAndUpdate(@Param("dto") Position position);
}

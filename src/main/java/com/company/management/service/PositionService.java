package com.company.management.service;

import com.company.management.dto.PositionAddDTO;
import com.company.management.dto.PositionPageDTO;
import com.company.management.dto.PositionUpdateDTO;
import com.company.management.entity.PageBean;
import com.company.management.entity.Position;
import com.company.management.entity.Result;

import java.util.List;

public interface PositionService {

    /**
     * 分页查询职位列表
     *
     * @param positionPageDTO
     * @return
     */
    Result<PageBean<Position>> page(PositionPageDTO positionPageDTO);

    /**
     * 查询职位列表
     *
     * @param name   职位名称
     * @param deptId 部门id
     * @return
     */
    Result<List<Position>> list(String name, Integer deptId);

    /**
     * 根据id查询职位
     * @param id
     * @return
     */
    Result<Position> getById(Integer id);

    /**
     * 添加职位
     * @param positionAddDTO
     * @return
     */
    Result<String> add(PositionAddDTO positionAddDTO);

    /**
     * 修改职位
     * @param positionAddDTO
     * @return
     */
    Result<String> update(PositionUpdateDTO positionAddDTO);

    /**
     * 删除职位
     * @param id
     * @return
     */
    Result<String> delete(Integer id);
}

package com.company.management.service;

import com.company.management.dto.DeptPageDTO;
import com.company.management.entity.Dept;
import com.company.management.entity.PageBean;
import com.company.management.entity.Result;

import java.util.List;

public interface DeptService {

    /**
     * 分页查询部门列表
     *
     * @param deptPageDTO
     * @return
     */
    Result<PageBean<Dept>> page(DeptPageDTO deptPageDTO);

    /**
     * 查询所有部门
     *
     * @param name
     * @return
     */
    Result<List<Dept>> list(String name);

    /**
     * 新增部门
     *
     * @param name
     * @param description
     * @return
     */
    Result add(String name, String description);

    /**
     * 删除部门
     *
     * @param id
     * @return
     */
    Result delete(Integer id);

    /**
     * 修改部门
     *
     * @param id
     * @param name
     * @param description
     * @return
     */
    Result update(Integer id, String name, String description);

    /**
     * 根据id查询部门
     *
     * @param id
     * @return
     */
    Result<Dept> getById(Integer id);
}

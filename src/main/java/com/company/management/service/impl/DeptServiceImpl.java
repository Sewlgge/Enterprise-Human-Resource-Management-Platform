package com.company.management.service.impl;

import com.company.management.dto.DeptPageDTO;
import com.company.management.entity.Dept;
import com.company.management.entity.PageBean;
import com.company.management.entity.Result;
import com.company.management.entity.User;
import com.company.management.mapper.DeptMapper;
import com.company.management.mapper.UserMapper;
import com.company.management.service.DeptService;
import com.company.management.utils.ThreadLocalUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptMapper deptMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Result<PageBean<Dept>> page(DeptPageDTO deptPageDTO) {
        log.info("分页查询部门列表");
        PageBean<Dept> pageBean = new PageBean<>();
        PageHelper.startPage(deptPageDTO.getPageNum(), deptPageDTO.getPageSize());
        List<Dept> deptList = deptMapper.list(deptPageDTO);
        //Page中提供了方法，可以获取PageHelper分页查询后    得到的记录和条数
        Page<Dept> p = (Page<Dept>) deptList;
        //把数据填充到PageBean对象中
        pageBean.setTotal(p.getTotal());
        pageBean.setItems(p.getResult());
        return Result.success(pageBean);
    }

    @Override
    public Result<List<Dept>> list(String name) {
        log.info("查询所有部门");
        DeptPageDTO deptPageDTO = new DeptPageDTO();
        deptPageDTO.setName(name);
        List<Dept> deptList = deptMapper.list(deptPageDTO);
        return Result.success(deptList);
    }

    @Override
    @Transactional
    public Result add(String name, String description) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        User user = userMapper.findByUserId(userId);
        if (user.getRole() != 0) {
            log.info("权限不足");
            return Result.error("权限不足");
        }

        log.info("添加部门");
        if (name == null || name.trim().isEmpty()) {
            log.info("部门名称不能为空");
            return Result.error("部门名称不能为空");
        }
        if (description == null || description.trim().isEmpty()) {
            log.info("部门描述不能为空");
        }
        if (description.length() > 100) {
            log.info("部门描述不能超过100个字符");
            return Result.error("部门描述不能超过100个字符");
        }
        if (name.length() > 20) {
            log.info("部门名称不能超过20个字符");
            return Result.error("部门名称不能超过20个字符");
        }
        if (name.length() < 2) {
            log.info("部门名称不能少于2个字符");
            return Result.error("部门名称不能少于2个字符");
        }
        if (description.length() < 2) {
            log.info("部门描述不能少于2个字符");
            return Result.error("部门描述不能少于2个字符");
        }
        Dept dept =  deptMapper.getDeptByName(name);
        if (dept != null) {
            log.info("部门已存在");
            return Result.error("部门已存在");
        }

        Dept deletedDept = deptMapper.getDeptByNameIncludeDeleted(name);
        if (deletedDept != null) {
            deletedDept.setIsDeleted(0);
            deletedDept.setDescription(description);
            Boolean flag = deptMapper.updateDept(deletedDept);
            if (flag == false) {
                log.info("添加部门失败");
                return Result.error("添加部门失败");
            }
            log.info("部门恢复成功");
            return Result.success("部门添加成功");
        }
        name = name.trim();
        description = description.trim();

        Boolean flag = deptMapper.add(name,description);
        if ( flag == false) {
            log.info("添加部门失败");
            return Result.error("添加部门失败");
        }
        return Result.success();
    }

    @Override
    @Transactional
    public Result delete(Integer id) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        User user = userMapper.findByUserId(userId);
        if (user.getRole() != 0) {
            log.info("权限不足");
            return Result.error("权限不足");
        }
        if (id == null) {
            log.info("部门id不能为空");
            return Result.error("部门id不能为空");
        }
        log.info("删除部门");
        Boolean flag = deptMapper.delete(id);
        if (flag == false) {
            log.info("删除部门失败");
            return Result.error("删除部门失败");
        }
        return Result.success("删除部门成功");
    }

    @Override
    @Transactional
    public Result update(Integer id, String name, String description) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        User user = userMapper.findByUserId(userId);
        if (user.getRole() != 0) {
            log.info("权限不足");
            return Result.error("权限不足");
        }
        return null;
    }

    @Override
    public Result<Dept> getById(Integer id) {
        log.info("查询部门");
        Dept dept = deptMapper.getById(id);
        return Result.success(dept);
    }
}

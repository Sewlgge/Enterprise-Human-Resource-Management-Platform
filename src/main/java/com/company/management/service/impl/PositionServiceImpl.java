package com.company.management.service.impl;

import com.company.management.dto.DeptPageDTO;
import com.company.management.dto.PositionAddDTO;
import com.company.management.dto.PositionPageDTO;
import com.company.management.dto.PositionUpdateDTO;
import com.company.management.entity.*;
import com.company.management.mapper.DeptMapper;
import com.company.management.mapper.PositionMapper;
import com.company.management.mapper.UserMapper;
import com.company.management.service.PositionService;
import com.company.management.utils.ThreadLocalUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PositionServiceImpl implements PositionService {
    @Autowired
    private PositionMapper positionMapper;
    @Autowired
    private DeptMapper deptMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Result<PageBean<Position>> page(PositionPageDTO positionPageDTO) {
        log.info("分页查询职位列表");
        PageBean<Position> pageBean = new PageBean<>();
        PageHelper.startPage(positionPageDTO.getPageNum(), positionPageDTO.getPageSize());
        List<Position> positionList = positionMapper.list(positionPageDTO);
        Map<Integer, String> deptMap = deptMapper.list(new DeptPageDTO()).stream()
                .collect(Collectors.toMap(Dept::getId, Dept::getName));
        positionList.forEach(position -> {
            position.setDeptName(deptMap.get(position.getDeptId()));
        });
        //Page中提供了方法，可以获取PageHelper分页查询后    得到的记录和条数
        Page<Position> p = (Page<Position>) positionList;
        //把数据填充到PageBean对象中
        pageBean.setTotal(p.getTotal());
        pageBean.setItems(p.getResult());
        return Result.success(pageBean);
    }

    @Override
    public Result<List<Position>> list(String name, Integer deptId) {
        log.info("查询职位列表");
        PositionPageDTO positionPageDTO = new PositionPageDTO();
        positionPageDTO.setName(name);
        positionPageDTO.setDeptId(deptId);
        List<Position> positionList = positionMapper.list(positionPageDTO);
        Map<Integer, String> deptMap = deptMapper.list(new DeptPageDTO()).stream()
                .collect(Collectors.toMap(Dept::getId, Dept::getName));
        positionList.forEach(position -> {
            position.setDeptName(deptMap.get(position.getDeptId()));
        });
        log.info("职位列表：{}", positionList);
        return Result.success(positionList);
    }

    @Override
    public Result<Position> getById(Integer id) {
        log.info("根据id查询职位");
        Position position = positionMapper.getById(id);
        Map<Integer, String> deptMap = deptMapper.list(new DeptPageDTO()).stream()
                .collect(Collectors.toMap(Dept::getId, Dept::getName));
        position.setDeptName(deptMap.get(position.getDeptId()));
        log.info("职位：{}", position);
        return Result.success(position);
    }

    @Override
    @Transactional
    public Result<String> add(PositionAddDTO positionAddDTO) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        User user = userMapper.findByUserId(userId);
        if (user.getRole() != 0) {
            log.info("权限不足");
            return Result.error("权限不足");
        }
        log.info("添加职位: {}", positionAddDTO);

        // 查询是否存在同名职位（包含已删除的）
        Position existingPosition = positionMapper.getByNameIncludeDeleted(positionAddDTO.getName());

        if (existingPosition != null) {
            // 如果存在同名职位，检查是否为软删除状态
            if (existingPosition.getIsDeleted() == 1) {
                // 恢复已删除的职位并更新信息
                Position position = new Position();
                position.setId(existingPosition.getId());
                position.setName(positionAddDTO.getName());
                position.setDescription(positionAddDTO.getDescription());
                position.setDeptId(positionAddDTO.getDeptId());

                Boolean flag = positionMapper.restoreAndUpdate(position);
                if (flag == false) {
                    return Result.error("添加失败");
                }
                log.info("恢复已删除的职位: {}", existingPosition.getId());
                return Result.success("添加成功");
            } else {
                // 职位已存在且未删除
                log.info("职位已存在: {}", positionAddDTO.getName());
                return Result.error("职位名称已存在");
            }
        }

        // 不存在同名职位，直接添加
        Position position = new Position();
        position.setDeptId(positionAddDTO.getDeptId());
        position.setName(positionAddDTO.getName());
        position.setDescription(positionAddDTO.getDescription());
        Boolean flag = positionMapper.add(position);
        if (flag == false) {
            return Result.error("添加失败");
        }
        return Result.success("添加成功");
    }

    @Override
    @Transactional
    public Result<String> update(PositionUpdateDTO positionAddDTO) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        User user = userMapper.findByUserId(userId);
        if (user.getRole() != 0) {
            log.info("权限不足");
            return Result.error("权限不足");
        }
        Boolean flag = positionMapper.update(positionAddDTO);
        if (flag == false) {
            return Result.error("修改失败");
        }
        return Result.success("修改成功");
    }

    @Override
    @Transactional
    public Result<String> delete(Integer id) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        User user = userMapper.findByUserId(userId);
        if (user.getRole() != 0) {
            log.info("权限不足");
            return Result.error("权限不足");
        }
        Boolean flag = positionMapper.delete(id);
        if (flag == false) {
            return Result.error("删除失败");
        }
        return Result.success("删除成功");
    }
}

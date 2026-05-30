package com.company.management.service.impl;

import com.company.management.dto.LogPageDTO;
import com.company.management.entity.Log;
import com.company.management.entity.PageBean;
import com.company.management.entity.Result;
import com.company.management.entity.User;
import com.company.management.mapper.LogMapper;
import com.company.management.mapper.UserMapper;
import com.company.management.service.LogService;
import com.company.management.utils.ThreadLocalUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class LogServiceImpl implements LogService {
    @Autowired
    private LogMapper logMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Result<PageBean<Log>> page(LogPageDTO logPageDTO) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        User user = userMapper.findByUserId(userId);
        if (user.getRole() != 0) {
            log.info("权限不足");
            return Result.error("权限不足");
        }
        PageBean<Log> logPageBean = new PageBean<>();
        PageHelper.startPage(logPageDTO.getPageNum(), logPageDTO.getPageSize());
        List<Log> logList = logMapper.list(logPageDTO);
        Page p = (Page) logList;
        logPageBean.setItems(logList);
        logPageBean.setTotal(p.getTotal());
        return Result.success(logPageBean);
    }
}

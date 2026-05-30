package com.company.management.service.impl;

import com.company.management.entity.Result;
import com.company.management.entity.Static;
import com.company.management.entity.User;
import com.company.management.mapper.StaticMapper;
import com.company.management.mapper.UserMapper;
import com.company.management.service.StaticService;
import com.company.management.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class StaticServiceImpl implements StaticService {

    @Autowired
    private StaticMapper staticMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Result<Static> staticborad() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        User user = userMapper.findByUserId(userId);
        if (user.getRole() != 0) {
            log.info("权限不足");
            return Result.error("权限不足");
        }
        Static aStatic = staticMapper.staticborad();
        return Result.success(aStatic);
    }
}

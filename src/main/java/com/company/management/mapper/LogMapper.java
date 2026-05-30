package com.company.management.mapper;

import com.company.management.dto.LogPageDTO;
import com.company.management.entity.Log;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LogMapper {
    /**
     * 查询所有日志
     */
    List<Log> list(@Param("dto") LogPageDTO logPageDTO);
}

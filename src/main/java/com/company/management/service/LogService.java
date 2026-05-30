package com.company.management.service;

import com.company.management.dto.LogPageDTO;
import com.company.management.entity.Log;
import com.company.management.entity.PageBean;
import com.company.management.entity.Result;

public interface LogService {
    /**
     * 分页查询日志
     */
    Result<PageBean<Log>> page(LogPageDTO logPageDTO);
}

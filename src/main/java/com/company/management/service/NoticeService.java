package com.company.management.service;

import com.company.management.dto.NoticePageDTO;
import com.company.management.entity.Notice;
import com.company.management.entity.PageBean;
import com.company.management.entity.Result;

public interface NoticeService {

    /**
     * 分页查询公告
     */
    Result<PageBean<Notice>> page(NoticePageDTO noticePageDTO);
}

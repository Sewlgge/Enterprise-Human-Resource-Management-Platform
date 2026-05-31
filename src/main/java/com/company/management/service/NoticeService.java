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

    /**
     * 公告详情
     */
    Result<Notice> detail(Integer id);

    /**
     * 获取当前用户草稿
     */
    Result<Notice> myDraft();

    /**
     * 发布或保存公告
     */
    Result publish(Notice notice);

    /**
     * 删除公告
     */
    Result delete(Integer id);
}

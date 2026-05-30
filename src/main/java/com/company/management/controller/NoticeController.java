package com.company.management.controller;

import com.company.management.dto.NoticePageDTO;
import com.company.management.entity.Notice;
import com.company.management.entity.PageBean;
import com.company.management.entity.Result;
import com.company.management.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notice")
@Validated
@Tag(name = "公告模块")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping("/page")
    @Operation(summary = "分页查询公告")
    public Result<PageBean<Notice>> page(NoticePageDTO noticePageDTO) {
        return noticeService.page(noticePageDTO);
    }

    @GetMapping("/{id}")
    @Operation(summary = "公告详情")
    public Result<Notice> detail(@PathVariable("id") Integer id) {
        return noticeService.detail(id);
    }

}

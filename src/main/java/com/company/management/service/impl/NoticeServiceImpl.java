package com.company.management.service.impl;

import com.company.management.dto.EmployeePageDTO;
import com.company.management.dto.NoticePageDTO;
import com.company.management.entity.Notice;
import com.company.management.entity.PageBean;
import com.company.management.entity.Result;
import com.company.management.entity.Employee;
import com.company.management.mapper.EmployeeMapper;
import com.company.management.mapper.NoticeMapper;
import com.company.management.service.NoticeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NoticeServiceImpl implements NoticeService {
    @Autowired
    private NoticeMapper noticeMapper;
    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 分页查询公告
     * @param noticePageDTO
     * @return
     */
    @Override
    public Result<PageBean<Notice>> page(NoticePageDTO noticePageDTO) {
        PageBean<Notice> noticePageBean = new PageBean<>();
        PageHelper.startPage(noticePageDTO.getPageNum(), noticePageDTO.getPageSize());
        List<Notice> noticeList = noticeMapper.list(noticePageDTO);
        Map<Integer, String> employeeMap = employeeMapper.list(new EmployeePageDTO())
                .stream().collect(Collectors.toMap(Employee::getId, Employee::getRealName));
        noticeList.forEach(notice -> notice.setPublisherName(employeeMap.get(notice.getPublisherId())));
        Page<Notice> p = (Page<Notice>) noticeList;
        noticePageBean.setTotal(p.getTotal());
        noticePageBean.setItems(p.getResult());
        return Result.success(noticePageBean);
    }

    @Override
    public Result<Notice> detail(Integer id) {
        Notice notice = noticeMapper.getById(id);
        return Result.success(notice);
    }
}

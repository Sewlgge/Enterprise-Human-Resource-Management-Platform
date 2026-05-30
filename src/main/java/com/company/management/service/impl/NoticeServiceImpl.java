package com.company.management.service.impl;

import com.company.management.dto.EmployeePageDTO;
import com.company.management.dto.NoticePageDTO;
import com.company.management.entity.*;
import com.company.management.mapper.EmployeeMapper;
import com.company.management.mapper.NoticeMapper;
import com.company.management.mapper.UserMapper;
import com.company.management.service.NoticeService;
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
public class NoticeServiceImpl implements NoticeService {
    @Autowired
    private NoticeMapper noticeMapper;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private UserMapper userMapper;

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

    @Override
    @Transactional
    public Result publish(Notice notice) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        User user = userMapper.findByUserId(userId);
        if (user.getRole() != 0) {
            log.info("权限不足");
            return Result.error("权限不足");
        }
        notice.setPublisherId(userId);
        int rows = noticeMapper.add(notice);
        if (rows != 1) {
            return Result.error("发布失败");
        }
        return Result.success("发布成功");
    }
}

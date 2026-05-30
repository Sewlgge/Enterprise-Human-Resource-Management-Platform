package com.company.management.mapper;

import com.company.management.dto.NoticePageDTO;
import com.company.management.entity.Notice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NoticeMapper {

    List<Notice> list(@Param("dto") NoticePageDTO noticePageDTO);
}

package com.company.management.mapper;

import com.company.management.dto.NoticePageDTO;
import com.company.management.entity.Notice;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NoticeMapper {

    /**
     * 分页查询公告
     */
    List<Notice> list(@Param("dto") NoticePageDTO noticePageDTO);

    /**
     * 根据id查询公告
     */
    @Select("select * from notice where id = #{id}")
    Notice getById(Integer id);

    /**
     * 新增公告
     */
    int add(@Param("dto") Notice notice);

    /**
     * 删除公告
     */
    @Delete("delete from notice where id = #{id}")
    int delete(Integer id);
}

package com.company.management.mapper;

import com.company.management.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.stream.DoubleStream;

@Mapper
public interface UserMapper {

    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    @Select("select * from sys_user where username = #{username} and is_deleted = 0")
    User findByUsername(@Param("username") String username);

    /**
     * 添加用户
     */
    @Insert("insert into sys_user(username, password) values(#{username}, #{password})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insertUser(User user);

    /**
     * 根据用户ID查询用户
     * @param userId
     * @return
     */
    @Select("select * from sys_user where id = #{userId} and is_deleted = 0")
    User findByUserId(@Param("userId") Integer userId);

    /**
     * 修改用户
     * @param userId
     * @param password
     */
    @Update("update sys_user set password = #{password} where id = #{userId}")
    void update(@Param("userId") Integer userId, @Param("password") String password);

    /**
     * 查询所有用户
     * @return
     */
    @Select("select * from sys_user where is_deleted = 0")
    List<User> list();
}

package com.example.user.mapper;

import com.example.user.pojo.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("select * from userInfo where id = #{id}")
    UserInfo queryById(Long id);

    //根据用户名查询
    @Select("select * from userInfo where username = #{username} and type= #{type}")
    UserInfo queryByUsername(String username,Integer type);

    @Select("select * from userInfo where Name = #{name}")
    UserInfo queryByName(String name);

    /**
     * 新增用户
     * @param user
     */
    @Insert("insert into userInfo(username, name, address, password,role,type) values(#{username}, #{name}, #{address}, #{password},#{role},#{type})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(UserInfo user);
}

package com.example.user.mapper;

import com.example.user.pojo.RolePermission;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 角色权限 实体类
 */

@Mapper
public interface RolePermissionMapper {

    /**
     * 根据编号查询 角色
     * @param id
     * @return
     */
    @Select("select * from rolePermission where id= #{id}")
    RolePermission queryById(Long id);

    /**
     * 根据 角色名查询权限
     * @param name
     * @return
     */
    @Select("select * from rolePermission where name= #{name}")
    List<RolePermission> queryByName(String name);

    /**
     * 新增角色权限
     * @param role
     */
    @Insert("insert info rolePermission(name,permission) values( #{name},#{permission})")
    void insert(RolePermission role);


}

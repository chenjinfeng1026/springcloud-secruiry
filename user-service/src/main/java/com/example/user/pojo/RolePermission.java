package com.example.user.pojo;

import lombok.Data;

@Data
public class RolePermission {
    /**
     * 编号
     */
    private Long id;

    /**
     * 角色名
     */
    private String name;

    /**
     * 权限
     */
    private String permission;

}

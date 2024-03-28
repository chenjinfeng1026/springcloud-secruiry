package com.example.feign.pojo;

import lombok.Data;

import java.util.List;

@Data
public class UserInfo {
    /**
     * 编号
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 姓名
     */
    private String name;

    /**
     * 地址
     */
    private String address;

    /**
     * 密码
     */
    private String password;

    /**
     * 角色
     */
    private String role;

    /**
     * 权限
     */
    private List<String> permissions;

    /**
     * 类型：0：后台用户 1：github用户 2：LDAP用户
     */
    private Integer type;
}

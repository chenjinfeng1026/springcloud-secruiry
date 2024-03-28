package com.example.user.service;

import com.example.user.mapper.RolePermissionMapper;
import com.example.user.mapper.UserMapper;
import com.example.user.pojo.RolePermission;
import com.example.user.pojo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RolePermissionMapper roleMapper;

    public UserInfo queryById(Long id)
    {
        //1、查询用户基本信息
        UserInfo userInfo = userMapper.queryById(id);

        //2、查询用户角色权限
        if (userInfo.getRole().length()>0){
            List<RolePermission> roles = roleMapper.queryByName(userInfo.getRole());

            List<String> permissions = new ArrayList<>();
            for (int i = 0; i < roles.size(); i++) {
                permissions.add(roles.get(i).getPermission());
            }
            userInfo.setPermissions(permissions);
        }
        return userInfo;
    }

    public UserInfo queryByUsername(String username,Integer type){
        //1、查询用户基本信息
        UserInfo userInfo = userMapper.queryByUsername(username,type);
        if (userInfo!=null&&userInfo.getId()>0) {
            //2、查询用户角色权限
            if (userInfo.getRole().length() > 0) {
                List<RolePermission> roles = roleMapper.queryByName(userInfo.getRole());

                List<String> permissions = new ArrayList<>();
                for (int i = 0; i < roles.size(); i++) {
                    permissions.add(roles.get(i).getPermission());
                }
                userInfo.setPermissions(permissions);
            }

            return userInfo;
        }else {
            return null;
        }
    }

    public UserInfo queryByLoginName(String loginName){
        return userMapper.queryByName(loginName);
    }

    public void addUser(UserInfo user) {
        userMapper.insert(user);
    }

    /**
     * 验证UAA 账号密码登录
     * @param username
     * @param password
     * @return
     */
    public UserInfo LoginByUsernameAndPassword(String username, String password,Integer type) {
        UserInfo user = userMapper.queryByUsername(username,type);
        //对密码进行MD5加密验证
        String password_md5 = getMD5Hash(password);
        if (user != null && user.equals(password_md5)) {
            return user;
        }
        return null;
    }

    public static String getMD5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}

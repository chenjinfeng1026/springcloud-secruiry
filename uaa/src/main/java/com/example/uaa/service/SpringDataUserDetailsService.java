package com.example.uaa.service;

import com.example.feign.clients.UserClient;
import com.example.feign.pojo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpringDataUserDetailsService implements UserDetailsService {

    @Autowired
    private UserClient userClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //验证登录用户的账号密码
        UserInfo userInfo = userClient.queryByLoginName(username,0);
        if (userInfo == null){
            return null;
        }
        //根据用户的id查询用户的权限
        String[] permissionArray = null;
        List<String> permissions = userInfo.getPermissions();
        if (permissions.size()>0) {
            permissionArray = new String[permissions.size()];
            permissions.toArray(permissionArray);
        }

        //构建security用户信息
        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(userInfo.getUsername())
                .password(userInfo.getPassword())
                .authorities(permissionArray).build();
        return userDetails;
    }
}

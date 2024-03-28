//package com.example.user;
//
//import com.example.user.pojo.UserInfo;
//import com.example.user.service.UserService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@SpringBootTest
//public class UserTest {
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private UserService userService;
//
//    @Test
//    public void AddUser(){
//        UserInfo userInfo = new UserInfo();
//        userInfo.setUsername("adm_1");
//        userInfo.setName("adm_1");
//        userInfo.setAddress("中国");
//        userInfo.setPassword(passwordEncoder.encode("adm_1"));
//
//        userService.addUser(userInfo);
//    }
//
//    @Test
//    public void FindUser(){
//        UserInfo userInfo = userService.queryById(1L);
//        System.out.println(userInfo.toString());
//    }
//}

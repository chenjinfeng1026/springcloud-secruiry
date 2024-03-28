package com.example.user.web;

import com.alibaba.fastjson.JSONObject;
import com.example.user.base.ResultJson;
import com.example.user.pojo.UserInfo;
import com.example.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 路径： /user/110
     *
     * @param id 用户id
     * @return 用户
     */
    @GetMapping("id/{id}")
    public UserInfo queryById(@PathVariable("id") Long id) {
        return userService.queryById(id);
    }

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    @GetMapping("/login/{username}/{password}")
    public UserInfo LoginByUsernameAndPassword(@PathVariable("username") String username,
                                               @PathVariable("password") String password,
                                               @PathVariable("type") Integer type) {
        return userService.LoginByUsernameAndPassword(username, password,type);
    }

    @GetMapping("/query")
    public UserInfo queryByLoginName(@RequestParam(value = "username",defaultValue = "",required = true) String username,
                                     @RequestParam(value = "type",defaultValue = "0",required = false) Integer type) {
        return userService.queryByUsername(username,type);
    }

    /**
     * 新增用户
     */
    @RequestMapping("add")
    public ResultJson addUser(@RequestParam(value = "username") String username,
                              @RequestParam(value = "Name") String name,
                              @RequestParam(value = "address") String address,
                              @RequestParam(value = "password") String password,
                              @RequestParam(value = "role") String role,
                              @RequestParam(value = "type") Integer type)
    {
        //查询登录名是否重复
        UserInfo userInfo = userService.queryByUsername(username,type);
        if (userInfo!=null&&userInfo.getId()>0){
            return ResultJson.error("用户名重复！");
        }
        //新增用户
        userInfo = new UserInfo();
        userInfo.setUsername(username);
        userInfo.setName(name);
        userInfo.setAddress(address);
        userInfo.setAddress(address);
        userInfo.setPassword(passwordEncoder.encode(password));
        userInfo.setRole(role);
        userInfo.setType(type);
        userService.addUser(userInfo);

        Long userId = userInfo.getId();

        JSONObject return_json = new JSONObject();
        return_json.put("userId",userId);
        return ResultJson.success(return_json);
    }
}

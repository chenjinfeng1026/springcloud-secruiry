package com.example.feign.clients;

import com.example.feign.base.ResultJson;
import com.example.feign.pojo.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "userservice")
public interface UserClient {

    @GetMapping("/user/id/{id}")
    UserInfo findById(@PathVariable("id") Long id);

    /**
     * 验证用户登录
     * @param username
     * @param password
     * @return
     */
    @GetMapping("/user/login/{username}/{password}")
    UserInfo login(@PathVariable("username") String username, @PathVariable("password") String password);

    @GetMapping("/user/query")
    UserInfo queryByLoginName(@RequestParam(value = "username",defaultValue = "",required = true) String username,
                              @RequestParam(value = "type",defaultValue = "0",required = false) Integer type);

    @GetMapping("/user/add")
    ResultJson addUser(@RequestParam(value = "username") String username,
                       @RequestParam(value = "Name") String name,
                       @RequestParam(value = "address") String address,
                       @RequestParam(value = "password") String password,
                       @RequestParam(value = "role") String role,
                       @RequestParam(value = "type") Integer type);
}

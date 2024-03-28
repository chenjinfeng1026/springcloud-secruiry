package com.example.uaa.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.feign.base.ResultJson;
import com.example.feign.clients.UserClient;
import com.example.feign.pojo.UserInfo;
import com.example.uaa.util.LdapUtil;
import com.example.uaa.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
@Slf4j
@RestController
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    private TokenStore tokenStore;

    @Autowired
    public UserClient userClient;

    @RequestMapping("/login-view")
    public ModelAndView login()
    {
        ModelAndView model = new ModelAndView();
        model.addObject("message", "Hello, Spring Boot!");
        model.setViewName("login/login-view");
        return model;
    }

    @RequestMapping("login-success")
    public JSONObject loginSuccess(){
        JSONObject object = new JSONObject();
        object.put("code",200);
        object.put("msg","登录成功");
        return object;
    }

    @RequestMapping("/login/github_callback")
    public ModelAndView githubCallback(@RequestParam(value = "code")String code){
        String url = "https://github.com/login/oauth/access_token";
        String param = "client_id=3237c2b58db02d960d49&client_secret=fe6978cc97afc32675f9b1c35b548295e685e97a&code="+code;
        String result = Util.connectUrl(url+"?"+param, "POST", "", null);
        log.debug("成功获取gitlab access_token："+result);

        String accessToken = result.split("&")[0].split("=")[1];
        url = "https://api.github.com/user";
        HashMap map = new HashMap();
        map.put("Authorization","Bearer " + accessToken);
        result = Util.connectUrl(url, "GET", "", map);
        log.debug("成功获取gitlab用户信息："+result);


        JSONObject gitlab_json = JSON.parseObject(result);
        //新增用户
        //查询用户是否存在
        UserInfo userInfo = userClient.queryByLoginName(gitlab_json.getString("login"),1);
        if (userInfo==null||userInfo.getId()==0) {

            ResultJson resultJson = userClient.addUser(gitlab_json.getString("login"), gitlab_json.getString("login"), "", "111111", "EDITOR", 1);
            if (resultJson.getCode() != 200) {
                //登录失败
                log.debug("git_lab 注册失败：" + gitlab_json.toJSONString() + ",result:" + resultJson.toString());
            } else {
                //登录成功
                log.debug("git_lab 注册成功：" + gitlab_json.toJSONString() + ",result:" + resultJson.toString());
            }
        }else {
            log.debug("git_lab 登录成功：" + gitlab_json.toJSONString());
        }

        ModelAndView model = new ModelAndView();
        model.addObject("message", "Hello, Spring Boot!");
        model.setViewName("index");
        return model;
    }

    /**
     * LDAP 用户登录
     * @param username
     * @param password
     * @return
     */
    @RequestMapping("login/ldap")
    public JSONObject Login_LDAP(@RequestParam(value = "username") String username,@RequestParam(value = "password") String password){
        JSONObject loginContext = LdapUtil.getLoginContext(username, password);
        if (loginContext.getIntValue("code")!=200){
            log.debug("ldap登录失败："+username);
        }else {
            //登录成功
            String username_ldap = loginContext.getJSONObject("data").getString("username");
            UserInfo userInfo = userClient.queryByLoginName(username_ldap,2);
            if (userInfo==null||userInfo.getId()==0) {

                ResultJson resultJson = userClient.addUser(username_ldap, username_ldap, "", password, "USER", 2);
                if (resultJson.getCode() != 200) {
                    //登录失败
                    log.debug("ldap 注册失败：" + username_ldap + ",result:" + resultJson.toString());
                } else {
                    //登录成功
                    log.debug("git_lab 注册成功：" + username_ldap + ",result:" + resultJson.toString());
                }
            }else {
                log.debug("git_lab 登录成功：" + username_ldap);
            }
        }

        JSONObject object = new JSONObject();
        object.put("code",200);
        object.put("msg","登录成功："+username);
        return object;
    }

//    @RequestMapping("/login-custom")
//    public String loginCustom(@RequestParam(value = "username")String username,
//                              @RequestParam(value = "password")String password,
//                              @RequestParam(value = "clientId",defaultValue = "c1")String clientId,
//                              HttpServletRequest httpServletRequest)
//    {
//
//
//        User userInfo = new User();
//        userInfo.setLoginName(username);
//
//
//        String[] authorities = new String[]{"p1"};
//
//
//        //将用户信息和权限 填充 到用户身份 token对象中
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userInfo, null, AuthorityUtils.createAuthorityList(authorities));
//        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
////            //将 authenticationToken 填充到 安全上下文中
//        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//
//
//
//        // 创建 OAuth2 授权请求
//        OAuth2Request oauth2Request = new OAuth2Request(null, clientId, null, true, null, null, null, null, null);
//
//        // 创建用户认证信息
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null);
//        OAuth2Authentication authentication = new OAuth2Authentication(oauth2Request, authenticationToken);
//
//        // 设置客户端信息
//        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
//
//        // 创建访问令牌
//        DefaultTokenServices tokenServices = new DefaultTokenServices();
//        tokenServices.setTokenStore(tokenStore);
//        tokenServices.setSupportRefreshToken(true);
//        tokenServices.setClientDetailsService(clientDetailsService);
//        tokenServices.setAuthenticationManager(authenticationManager);
//
//        OAuth2AccessToken accessToken = tokenServices.createAccessToken(authentication);
//
//
//
//        return accessToken1;
//    }


}

package com.example.product.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.feign.pojo.UserInfo;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nimbusds.jose.JWSObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

@Slf4j
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private TokenStore tokenStore;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
        //解析出头中的token
        String realToken = httpServletRequest.getHeader("Authorization");
        realToken = realToken.replace("Bearer ","");
        System.out.println("token:"+realToken);

        if (realToken!=null){
            OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(realToken);
            JWSObject jwsObject = null;
            jwsObject = JWSObject.parse(realToken);
            String userStr = jwsObject.getPayload().toString();

            JSONObject userInfo_object = JSON.parseObject(userStr);

            log.info("AuthGlobalFilter.filter() user:{}", userStr);
            log.info("AuthGlobalFilter.filter() oAuth2AccessToken expireTime:{},getAdditionalInformation:{}", oAuth2AccessToken.getExpiration(),oAuth2AccessToken.getAdditionalInformation());

            UserInfo userInfo = new UserInfo();
            userInfo.setUsername(userInfo_object.getString("user_name"));

            //用户权限信息
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(userStr, JsonObject.class);
            String[] authorities = gson.fromJson(jsonObject.get("authorities"),String[].class);
//
//
//            //将用户信息和权限 填充 到用户身份 token对象中
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userInfo, null, AuthorityUtils.createAuthorityList(authorities));
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
//            //将 authenticationToken 填充到 安全上下文中
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

}

package com.example.product.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    public static final String RESOURCE_ID = "res1";

    @Autowired
    private TokenStore tokenStore;

    public void configure(ResourceServerSecurityConfigurer resources){
        resources.resourceId(RESOURCE_ID)//资源ID
                 .tokenStore(tokenStore)
                 .stateless(true);

        //内存加载方式
//        resources.resourceId(RESOURCE_ID)//资源ID
//                .tokenServices(tokenService())//验证令牌的服务
//                .stateless(true);
    }

    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/**").access("#oauth2.hasScope('ROLE_API')")//验证颁发令牌的scope
                .and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);//Spring Security永远不会创建HttpSession，它不会使用HttpSession来获取SecurityContext

    }

    /**
     * 资源令牌 解析服务
     * @return
     */
//    @Bean
//    public ResourceServerTokenServices tokenService(){
//        //使用远程服务 请求 授权服务器 校验token，必须指定校验token的 url、client_id、client_secret
//        RemoteTokenServices services = new RemoteTokenServices();
//        services.setCheckTokenEndpointUrl("http://localhost:8081/oauth/check_token");
//        services.setClientId("c1");
//        services.setClientSecret("secret");
//        return services;
//    }

}

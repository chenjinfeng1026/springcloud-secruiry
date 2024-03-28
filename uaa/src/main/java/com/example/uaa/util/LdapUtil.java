package com.example.uaa.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.logging.Logger;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

@Slf4j
public class LdapUtil {
    private static DirContext ctx;

    // LDAP服务器端口默认为389
    private static final String LDAP_URL = "ldap://172.16.76.10:389";

    // ROOT根据此参数确认用户组织所在位置
    private static final String LDAP_PRINCIPAL = "cn=group,ou=test,dc=zeny,dc=com";

    // LDAP驱动
    private static final String LDAP_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";

    private static Logger logger = Logger.getLogger(String.valueOf(LdapUtil.class));


    // 通过连接LDAP服务器对用户进行认证，返回LDAP对象
    public static JSONObject getLoginContext(String account, String password) {
        JSONObject object = new JSONObject();
        object.put("code",500);
        object.put("msg","LDAP认证失败！");

        //开始认证
        Hashtable env = new Hashtable();
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_CREDENTIALS, password);
        // cn=属于哪个组织结构名称，ou=某个组织结构名称下等级位置编号
        env.put(Context.SECURITY_PRINCIPAL, "cn="+account+","+LDAP_PRINCIPAL);
        env.put(Context.INITIAL_CONTEXT_FACTORY, LDAP_FACTORY);
        env.put(Context.PROVIDER_URL, LDAP_URL);
        try {
            // 连接LDAP进行认证
            ctx = new InitialDirContext(env);
            String displayName = "";

            Attributes attributes = ctx.getAttributes("cn="+account+","+LDAP_PRINCIPAL);
            System.out.println(attributes.toString());
            if (attributes!=null) {
                Attribute attribute = attributes.get("sn");
                if (attribute!=null) {
                    displayName = (String) attribute.get();
                }
            }
            object.put("code",200);
            object.put("msg","认证成功！");
            JSONObject data = new JSONObject();
            data.put("username",displayName);
            object.put("data",data);
            logger.info("【" + account + "】用户于【" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "】登陆系统成功，"+displayName);
        } catch (javax.naming.AuthenticationException e) {
            System.out.println("认证失败");
        } catch (NamingException err) {
            System.out.println(err.toString());
            logger.info("--------->>【" + account + "】用户验证失败");
        } catch (Exception e) {
            System.out.println("认证出错：");
            e.printStackTrace();
        }
        return object;
    }
    // 关闭LDAP服务器连接
    public static void closeCtx() {
        try {
            ctx.close();
        } catch (NamingException ex) {
            logger.info("--------->> 关闭LDAP连接失败");
        }
    }
}

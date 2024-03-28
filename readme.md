#springcloud-security
##1、项目介绍
项目使用springcloud-security作为安全框架，使用springcloud-security-oauth2作为oauth2实现。集成了Feign、gateway、Nacos等服务。

##2、交付
##2.1 登录网址
http://172.16.76.10:7573/auth/login-view  

数据库中用户账号密码已创建：
普通用户：user_1/user_1
EDITOR：editor_1/editor_1
PRODUCT_ADMIN：adm_1/adm_1

GITHUB登录：
需在github中创建一个OAuth2应用，并获取ClientId和ClientSecret，在项目对原参数进行替换。（templates/login/login-view.html、uaa/src/main/java/com/example/uaa/controller/LoginController.java）

LDAP登录账号密码已创建：
普通用户：ldap_user_1/ldap_user_1
EDITOR：ldap_editor_1/ldap_editor_1
PRODUCT_ADMIN：ldap_adm_1/ldap_adm_1

![img.png](help_images/img1.png)
type=0为数据库 账号密码，type=1为github账号，type=2为ldap账号。


注：登录成功后，会跳转到 http://172.16.76.10:7573/auth/login-success，IP需根据部署的虚拟机IP进行调整。LDAP为单独部署，需修改配置文件（com/example/uaa/util/LdapUtil.java），已本地部署测试。
![img.png](help_images/img.png)

##2.2 功能实践
1、通过CURL获取ACCESS TOKEN：  
curl --location --request POST 'http://172.16.76.10:7573/auth/oauth/token?client_id=c1&client_secret=secret&grant_type=password&redirect_uri=http://www.baidu.com&username=chenjinfeng&password=123456'

![img.png](help_images/img2.png)
![img.png](help_images/img3.png)

2、通过CURL添加产品[通过ACCESS TOKEN鉴权]
curl --location --request GET 'http://172.16.76.10:7573/product/add?name=%E5%B0%8F%E7%B1%B3%E6%89%8B%E6%9C%BA&description=%E5%B0%8F%E7%B1%B3%E6%89%8B%E6%9C%BA%E5%BE%88%E8%80%90%E7%94%A8&address=%E4%B8%AD%E5%9B%BD&price=999' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzMSJdLCJ1c2VyX25hbWUiOiJjaGVuamluZmVuZyIsInNjb3BlIjpbIlJPTEVfQURNSU4iLCJST0xFX1VTRVIiLCJST0xFX0FQSSJdLCJleHAiOjE3MTE1OTU0NzcsImF1dGhvcml0aWVzIjpbInByb2R1Y3RfZWRpdCIsInByb2R1Y3RfbGlzdCIsInByb2R1Y3RfYWRkIiwicHJvZHVjdF9kZWxldGUiXSwianRpIjoiZjBjNmI5ZTYtMTcyYy00ODViLWJiNGQtYTMwYjliZWYyNjIyIiwiY2xpZW50X2lkIjoiYzEifQ.hZfR2qRjYXCpHLLVwiJ38JYvIB6UY21ZhW6X5Dtrf4Y'
![img.png](help_images/img4.png)

3、通过CURL修改产品[通过ACCESS TOKEN鉴权]
curl --location --request GET 'http://172.16.76.10:7573/product/update?name=%E5%B0%8F%E7%B1%B3%E6%89%8B%E6%9C%BA&description=%E5%B0%8F%E7%B1%B3%E6%89%8B%E6%9C%BA%E5%BE%88%E8%80%90%E7%94%A8&address=%E4%B8%AD%E5%9B%BD&price=1999&id=2' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzMSJdLCJ1c2VyX25hbWUiOiJjaGVuamluZmVuZyIsInNjb3BlIjpbIlJPTEVfQURNSU4iLCJST0xFX1VTRVIiLCJST0xFX0FQSSJdLCJleHAiOjE3MTE1OTU0NzcsImF1dGhvcml0aWVzIjpbInByb2R1Y3RfZWRpdCIsInByb2R1Y3RfbGlzdCIsInByb2R1Y3RfYWRkIiwicHJvZHVjdF9kZWxldGUiXSwianRpIjoiZjBjNmI5ZTYtMTcyYy00ODViLWJiNGQtYTMwYjliZWYyNjIyIiwiY2xpZW50X2lkIjoiYzEifQ.hZfR2qRjYXCpHLLVwiJ38JYvIB6UY21ZhW6X5Dtrf4Y'
![img.png](help_images/img5.png)

4、通过CURL删除产品[通过ACCESS TOKEN鉴权]
curl --location --request GET 'http://172.16.76.10:7573/product/delete?id=2' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzMSJdLCJ1c2VyX25hbWUiOiJjaGVuamluZmVuZyIsInNjb3BlIjpbIlJPTEVfQURNSU4iLCJST0xFX1VTRVIiLCJST0xFX0FQSSJdLCJleHAiOjE3MTE1OTU0NzcsImF1dGhvcml0aWVzIjpbInByb2R1Y3RfZWRpdCIsInByb2R1Y3RfbGlzdCIsInByb2R1Y3RfYWRkIiwicHJvZHVjdF9kZWxldGUiXSwianRpIjoiZjBjNmI5ZTYtMTcyYy00ODViLWJiNGQtYTMwYjliZWYyNjIyIiwiY2xpZW50X2lkIjoiYzEifQ.hZfR2qRjYXCpHLLVwiJ38JYvIB6UY21ZhW6X5Dtrf4Y'
![img.png](help_images/img6.png)

5、查询所有产品[通过ACCESS TOKEN鉴权]

![img.png](help_images/img7.png)

# mybatis_sql   说明文档
Spring Boot+MySQL+Mybatis+Maven+IDEA（增删改查、登录登出、token）
学习笔记；

# 登录（post）：http://localhost:8008/login
    用户名登录：
        入参          :     值
        loginType     :username
        username      :yz
        password      :123456a
    手机号登录：
        入参          :     值
        loginType     :     mobile
        mobile        :     18888888888
        authCode      :     123456
        
    响应：Token
# 请求（get）：http://localhost:8008/user/test
    传入Token

# 登出（get|post|put|delete）：http://localhost:8008/logout
    传入Token
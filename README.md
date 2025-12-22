# 元识小程序saas平台
一个基于 java 21的项目，旨在为广大用户提供一个简单、易用的小程序。

## 运行环境
- JDK 21
- MySQL 8.0.27

## 项目结构
- `src/main/java/com/yl/yuansaas`：主要代码目录
- `src/main/java/com/yl/yuansaas/common`：通用代码目录
- `src/main/java/com/yl/yuansaas/core`：核心代码目录,框架配置
- `src/main/resources`：配置文件目录
- `src/test/java/com/yl/yuansaas/***`：业务代码
- `pom.xml`：maven 依赖管理文件

## 运行方式

1. 配置数据库连接信息
2. java -jar xxx.jar
3. 访问 http://localhost:19171/yuansaas

## 框架功能使用
### Response
统一的响应格式：
```json
{
  "status": "SUCCESS",
  "code": 0,
  "data": "hello world",
  "message": "hello world",
  "timestamp": 1753668355233
}
```
#### 使用方法
- ResponseBuilder.okResponse("hello world" , "hello world"); // 成功响应
- ResponseBuilder.failResponse("hello world" , "hello world"); // 失败响应
- ResponseBuilder.errorResponse("hello world" , "hello world"); // 错误响应


### 异常处理
非业务功能的异常HTTP Status code全部会返回非200状态码，
```json
{
        "status": "ERROR",   // 状态码 ERROR 、FAIL 、SUCCESS
        "code": 0,   // 错误码 默认为0 每个业务模块定义自己的错误码
        "data": {},
        "message": "测试",   // 错误信息
        "timestamp": 1753668051978
}
```

#### 使用方法
- throw AppException.builder().errorData( model).message("测试").build();

### JSON序列化
统一使用JSON序列化工具，使用Jackson, 序列户统一使用工具类JacksonUtil

### 上下文
统一上下文对象，AppContext
上下文持有者：AppContextHolder，用户获取上下文基本信息等
上下文工具类：AppContextUtil, 用于获取上下文对象、上下文辅助工具类等

### 循环引用
在项目中禁止出现循环引用。如果有这种情况，引入第三个服务来打破循环引用。

## 接口文档
执行如下命令生成接口文档：
```shell
 mvn -Dfile.encoding=UTF-8 smart-doc:html
```

生成后访问地址：http://localhost:63342/yuansaas/static/doc/index.html


## 贡献者
- HTB
  
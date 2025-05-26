# JiuHou Blogger Backend

基于Spring Cloud的博客系统后台服务

## 项目结构

```
jiuhou-blogger
├── jiuhou-common    -- 公共模块
├── jiuhou-gateway   -- 网关服务
├── jiuhou-auth      -- 认证服务
└── jiuhou-admin     -- 管理服务
```

## 技术栈

- Spring Boot 3.2.3
- Spring Cloud 2023.0.0
- Spring Cloud Alibaba 2022.0.0.0
- Spring Security + JWT
- MyBatis-Plus 3.5.5
- PostgreSQL
- Redis
- Nacos
- Knife4j
- Maven
- Docker

## 环境要求

- JDK 17+
- Maven 3.8+
- PostgreSQL 14+
- Redis 6+
- Nacos 2.2+

## 本地开发

1. 安装并启动PostgreSQL
2. 安装并启动Redis
3. 下载并启动Nacos服务
4. 修改各个服务的配置文件中的数据库连接信息
5. 依次启动服务：
   - Nacos
   - Gateway
   - Auth
   - Admin

## 打包部署

```bash
# 打包
mvn clean package

# 使用Docker Compose启动（待补充）
docker-compose up -d
```

## 接口文档

启动服务后访问：http://localhost:8080/doc.html

## 项目特性

- 基于Spring Cloud Alibaba的微服务架构
- 统一的网关服务，实现路由转发、访问控制
- 基于JWT的认证授权
- 服务注册与配置中心（Nacos）
- 服务间通信（OpenFeign）
- 统一的异常处理
- 统一的接口文档（Knife4j）
- 数据库读写分离（可选） 
# JiuHou Blogger 博客系统

基于 Spring Cloud 的微服务博客系统。

## 技术栈

- **Spring Boot**: 3.2.3
- **Spring Cloud**: 2023.0.0
- **Spring Cloud Alibaba**: 2022.0.0.0
- **数据库**: PostgreSQL
- **ORM**: MyBatis Plus 3.5.5
- **缓存**: Redis
- **认证**: JWT
- **文档**: Knife4j 4.5.0

## 项目结构

```
jiuhou-blogger
├── jiuhou-common            -- 公共模块
├── jiuhou-eureka-server     -- 服务注册中心
├── jiuhou-config-server     -- 配置中心
├── jiuhou-auth             -- 认证授权服务
├── jiuhou-admin            -- 后台管理服务
└── jiuhou-blog-service     -- 博客核心服务
```

## 模块说明

1. **jiuhou-common**: 
   - 公共工具类
   - 通用实体类
   - 统一响应处理
   - 全局异常处理

2. **jiuhou-eureka-server**:
   - 服务注册与发现中心
   - 服务健康检查
   - 服务状态监控

3. **jiuhou-config-server**:
   - 集中配置管理
   - 配置动态刷新
   - 环境隔离

4. **jiuhou-auth**:
   - 用户认证
   - JWT 令牌管理
   - 权限控制

5. **jiuhou-admin**:
   - 用户管理
   - 角色管理
   - 权限管理
   - 系统配置

6. **jiuhou-blog-service**:
   - 文章管理
   - 分类管理
   - 标签管理
   - 评论管理

## 启动顺序

1. **启动基础服务**
   ```bash
   # 1. 启动 Eureka Server
   cd jiuhou-eureka-server
   mvn spring-boot:run

   # 2. 启动 Config Server
   cd jiuhou-config-server
   mvn spring-boot:run
   ```

2. **启动核心服务**
   ```bash
   # 3. 启动认证服务
   cd jiuhou-auth
   mvn spring-boot:run

   # 4. 启动博客服务
   cd jiuhou-blog-service
   mvn spring-boot:run

   # 5. 启动管理服务
   cd jiuhou-admin
   mvn spring-boot:run
   ```

## 服务端口

| 服务名称 | 端口 | 说明 |
|---------|------|-----|
| eureka-server | 8761 | 服务注册中心 |
| config-server | 8888 | 配置中心 |
| auth-service | 9000 | 认证服务 |
| admin-service | 9100 | 管理服务 |
| blog-service | 9200 | 博客服务 |

## 开发建议

1. **开发顺序**:
   - 先完成 `jiuhou-common` 的基础功能
   - 实现 `jiuhou-auth` 的用户认证
   - 开发 `jiuhou-blog-service` 的核心功能
   - 最后完成 `jiuhou-admin` 的管理功能

2. **配置管理**:
   - 所有配置文件统一放在配置中心
   - 环境相关的配置使用配置中心的配置文件
   - 开发环境可以使用本地配置文件

3. **代码规范**:
   - 遵循阿里巴巴 Java 开发手册
   - 使用统一的代码格式化工具
   - 保持良好的代码注释

## 注意事项

1. 确保 PostgreSQL 数据库已启动
2. Redis 服务必须可用
3. 首次启动前需要初始化数据库
4. 配置中心的配置文件需要提前准备好

## 环境要求

- JDK 17+
- Maven 3.8+
- PostgreSQL 14+
- Redis 6+

## 开发工具

- IDE: IntelliJ IDEA (推荐)
- 数据库工具: DBeaver
- API 测试: Postman
- Git 工具: SourceTree

## 项目介绍

本项目是一个基于 Spring Cloud 2023.0.0 + Spring Boot 3.2.3 的微服务博客系统，采用了最新的技术栈进行开发。

### 技术选型

#### 后端技术

- 基础框架：Spring Boot 3.2.3
- 微服务框架：Spring Cloud 2023.0.0
- 服务注册与发现：Spring Cloud Netflix Eureka
- 网关服务：Spring Cloud Gateway
- 数据库：PostgreSQL 16
- 数据库版本管理：Flyway
- 工具库：Hutool、Apache Commons

#### 开发环境

- JDK 17
- Maven 3.8+
- PostgreSQL 16+
- Redis 6.0+

### 系统架构

```
jiuhou-blogger
├── jiuhou-common -- 公共模块
├── jiuhou-db -- 数据库脚本模块
├── jiuhou-gateway -- 网关服务
├── jiuhou-auth -- 认证服务
├── jiuhou-admin -- 管理服务
├── jiuhou-blog-service -- 博客核心服务
├── jiuhou-config-server -- 配置中心
└── jiuhou-eureka-server -- 服务注册中心
```

### 数据库设计

系统使用 PostgreSQL 数据库，包含以下主要数据表：

- sys_user：用户信息表
- blog_article：博客文章表
- blog_category：文章分类表
- blog_tag：文章标签表
- blog_article_tag：文章标签关联表
- blog_comment：文章评论表

### 快速开始

1. 克隆项目
```bash
git clone https://github.com/your-username/jiuhou-blogger.git
```

2. 创建数据库
```bash
cd jiuhou-db/scripts
chmod +x init-db.sh
./init-db.sh
```

3. 启动服务
按照以下顺序启动服务：
- jiuhou-eureka-server
- jiuhou-config-server
- jiuhou-gateway
- jiuhou-auth
- jiuhou-admin
- jiuhou-blog-service

### 项目特点

1. 采用最新的 Spring Cloud 2023.0.0 版本
2. 使用 Spring Cloud Gateway 作为网关
3. 使用 PostgreSQL 作为数据库，支持更丰富的数据类型和功能
4. 集成 Flyway 进行数据库版本管理
5. 统一的异常处理和接口规范
6. 完善的日志记录和监控

### 接口文档

接口文档使用 Swagger 3.0 (OpenAPI)，启动服务后访问：
- 网关聚合文档：http://localhost:8080/doc.html
- 各服务文档：http://localhost:对应服务端口/doc.html

### 贡献指南

1. Fork 本仓库
2. 新建 feature_xxx 分支
3. 提交代码
4. 新建 Pull Request

### 开源协议

本项目使用 MIT 协议开源，请自由地享受和参与开源。 
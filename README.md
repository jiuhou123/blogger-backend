# JiuHou Blog System (九侯博客系统)

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2023.0.0-brightgreen.svg)](https://spring.io/projects/spring-cloud)
[![JDK](https://img.shields.io/badge/JDK-17-orange.svg)](https://www.oracle.com/java/technologies/downloads/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](./LICENSE)

[English](./README.en.md) | 简体中文

## 📚 项目介绍

JiuHou Blog System 是一个基于 Spring Cloud 微服务架构的现代化博客系统后端。系统采用了最新的 Spring 技术栈，提供了完整的博客功能支持，包括文章管理、用户管理、评论系统等核心功能。

### 🎯 主要特性

- 基于 Spring Cloud 的微服务架构
- 统一的接口网关和鉴权中心
- 完整的权限管理系统
- 服务注册与配置中心
- 分布式事务支持
- 服务监控和链路追踪
- 统一的接口文档
- 支持容器化部署

## 🔨 技术栈

### 后端技术
- Spring Boot 3.2.3
- Spring Cloud 2023.0.0
- Spring Cloud Alibaba 2022.0.0.0
- Spring Security + JWT
- MyBatis-Plus 3.5.5
- PostgreSQL
- Redis

### 微服务组件
- Nacos：服务注册与配置中心
- Gateway：API 网关
- OpenFeign：服务间通信
- Sentinel：服务限流降级
- Seata：分布式事务

### 开发工具
- Maven 3.8+
- Docker & Docker Compose
- Knife4j：接口文档

## 📦 项目结构

```
jiuhou-blogger
├── jiuhou-common          -- 公共工具模块
├── jiuhou-gateway         -- 网关服务
├── jiuhou-auth            -- 认证服务
├── jiuhou-admin           -- 后台管理服务
├── jiuhou-blog-service    -- 博客核心服务
├── jiuhou-eureka-server   -- 服务注册中心
└── jiuhou-config-server   -- 配置中心服务
```

## 🚀 快速开始

### 环境要求

- JDK 17+
- Maven 3.8+
- PostgreSQL 14+
- Redis 6+
- Nacos 2.2+
- Docker & Docker Compose（可选）

### 本地开发

1. 克隆项目
```bash
git clone https://github.com/yourusername/jiuhou-blogger.git
cd jiuhou-blogger
```

2. 配置环境
```bash
# 安装并启动 PostgreSQL
# 安装并启动 Redis
# 下载并启动 Nacos 服务
```

3. 修改配置
- 更新各服务模块中的 `application.yml` 配置文件
- 修改数据库连接信息
- 修改 Redis 连接信息

4. 编译项目
```bash
mvn clean package -DskipTests
```

5. 启动服务（按顺序）
```bash
# 1. 启动 Nacos
# 2. 启动 Gateway 服务
# 3. 启动 Auth 服务
# 4. 启动其他业务服务
```

### Docker 部署

```bash
# 构建镜像
docker-compose build

# 启动服务
docker-compose up -d
```

## 📚 文档

- 接口文档：http://localhost:8080/doc.html
- 详细文档：[Wiki](../../wiki)

## 🤝 贡献指南

1. Fork 本仓库
2. 创建您的特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交您的更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开一个 Pull Request

## 📄 开源协议

本项目使用 MIT 协议 - 查看 [LICENSE](./LICENSE) 文件了解详情

## 👨‍💻 维护者

- [@YourName](https://github.com/yourusername) - email@example.com

## 🙏 致谢

感谢以下开源项目：

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Cloud](https://spring.io/projects/spring-cloud)
- [MyBatis-Plus](https://baomidou.com/)
- [Nacos](https://nacos.io/) 
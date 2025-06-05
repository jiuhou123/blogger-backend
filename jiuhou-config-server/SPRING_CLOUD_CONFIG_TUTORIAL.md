# Spring Cloud Config Server 教学文档

## 1. 什么是 Spring Cloud Config Server？
Spring Cloud Config Server 是 Spring Cloud 体系下的分布式配置中心，支持将应用配置集中存储在 Git（本地或远程）等仓库中，所有微服务都可以动态拉取配置，实现配置的集中管理和动态刷新。

## 2. 典型架构与工作原理
- **配置仓库**：存放所有环境、服务的配置文件（如 yml/properties），可用本地文件夹或远程 Git 仓库。
- **Config Server**：作为服务端，负责从配置仓库拉取配置并对外暴露 HTTP 接口。
- **微服务客户端**：通过 HTTP 向 Config Server 拉取自己的配置。

```
[Git仓库] <——> [Config Server] <——> [各微服务]
```

## 3. 如何搭建本地 Config Server

### 依赖环境
- JDK 8 及以上
- Maven 3.6+

### 快速启动
1. 克隆本项目，进入 `jiuhou-config-server` 目录。
2. 配置 `src/main/resources/application.yml`，指定配置仓库地址（本地或远程）。
3. 启动服务：
   ```bash
   mvn spring-boot:run
   ```
4. 默认端口为 8888。

## 4. 配置本地/远程 Git 仓库作为配置源

### 本地仓库
```yaml
spring:
  cloud:
    config:
      server:
        git:
          uri: file:///C:/dev/workspace/config-repo
```

### 远程 GitHub 仓库（私有仓库需配置 Token）
```yaml
spring:
  cloud:
    config:
      server:
        git:
          uri: https://github.com/yourname/your-config-repo.git
          username: ${GITHUB_USERNAME}
          password: ${GITHUB_TOKEN}
```
- 启动前设置环境变量 GITHUB_TOKEN。

## 5. 如何修改配置并让服务生效
1. 在配置仓库（本地或远程）中编辑对应的 yml/properties 文件。
2. 如果是 Git 仓库，需 commit 并 push。
3. Config Server 会自动拉取最新配置（如配置了 `force-pull: true`），否则重启 Config Server。
4. 微服务重启后会拉取到最新配置。如集成 Spring Cloud Bus，可实现热刷新。

## 6. 典型接口说明与验证方法

- 健康检查：
  - `GET http://localhost:8888/actuator/health`  
    返回 `{ "status": "UP" }` 表示服务健康。
- 拉取配置：
  - `GET http://localhost:8888/{application}/{profile}`
    例：`http://localhost:8888/blogger-auth/dev`
- 需要认证时，使用 application.yml 中配置的账号密码（默认 admin/admin）。

## 7. 常见问题与排查
- 访问 404：Config Server 没有根路径，需访问 `/actuator/health` 或具体配置接口。
- 拉取配置失败：检查 Git 仓库地址、Token、search-paths 是否正确。
- 配置未生效：确认已 push 最新配置，Config Server 已拉取到最新内容。
- 端口冲突：修改 `server.port`。

## 8. 推荐进阶资料
- [Spring Cloud Config 官方文档](https://cloud.spring.io/spring-cloud-config/)
- [Spring Cloud Config 中文文档](https://springcloud.cc/spring-cloud-config.html)
- [Spring Cloud Bus 官方文档](https://cloud.spring.io/spring-cloud-bus/)

---
如有更多问题请联系项目维护者或查阅 docs 目录。 
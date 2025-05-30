# jiuhou-config-server

## 简介
本模块为 Spring Cloud Config 配置中心，集中管理各微服务的配置文件，支持从 GitHub 仓库动态拉取配置。

## 依赖环境
- JDK 8 及以上
- Maven 3.6+
- GitHub 访问令牌（私有仓库必需）
- 推荐先启动 PostgreSQL 数据库和 Eureka 注册中心

## 启动前准备

1. **配置 GitHub Token（如配置仓库为私有）**
   - 在 GitHub 个人设置中生成 Personal Access Token（只需 repo/read 权限）。
   - 启动前设置环境变量：
     - Windows PowerShell：
       ```powershell
       $env:GITHUB_TOKEN="你的真实GitHub Token"
       ```
     - Linux/WSL：
       ```bash
       export GITHUB_TOKEN=你的真实GitHub Token
       ```

2. **检查 application.yml 配置**
   - `spring.cloud.config.server.git.uri` 指向你的配置仓库地址。
   - `username`、`password` 推荐用环境变量注入，不要写死 Token。
   - `search-paths`、`default-label` 等参数根据实际仓库结构调整。

## 启动命令

```bash
mvn spring-boot:run
```

## 推荐启动顺序（本地开发）

1. 启动 PostgreSQL 数据库
2. 启动 config-server（本服务）
3. 启动 Eureka 注册中心
4. 启动各业务微服务
5. 启动前端/网关（如有）

## 常见问题

- **未配置 GITHUB_TOKEN 或 Token 无效**：拉取私有仓库会失败，服务无法启动。
- **search-paths 配置错误**：导致找不到配置文件，微服务启动报错。
- **端口冲突**：默认端口 8888，如被占用请在 application.yml 中修改。
- **配置仓库为公开仓库**：可去掉 username/password 配置。

## 参考文档
- [Spring Cloud Config 官方文档](https://cloud.spring.io/spring-cloud-config/)
- [GitHub Token 生成](https://github.com/settings/tokens)

---
如有更多问题请查阅 docs 目录或联系开发者。

## 主要功能

- 通过 Git 仓库集中管理配置
- 支持多环境（dev/test/prod）和多服务配置
- 支持配置热更新
- 通过安全认证保护配置接口

## 目录结构

- `src/main/resources/application.yml`：主配置文件，支持环境变量占位
- `src/main/resources/application-local.yml`：本地开发专用配置（已加入 .gitignore）
- `config-repo/`：实际的配置仓库（建议单独管理）

## 启动方式

### 本地开发
```bash
java -jar jiuhou-config-server.jar --spring.profiles.active=local
```
或在 IDE 中设置 `active profile` 为 `local` 直接运行主类。

### 生产/测试环境
```bash
java -jar jiuhou-config-server.jar
```
或通过环境变量/启动参数覆盖敏感信息。

## 常用接口

- 健康检查：`/actuator/health`
- 拉取配置：`/{application}/{profile}` 例：`/blogger-auth/dev`

## 注意事项

- 不要将敏感信息（如 token）提交到远程仓库
- 本地敏感配置请写在 `application-local.yml`
- 配置变更后建议重启服务或使用 Spring Cloud Bus 刷新
- `application.yml` 支持环境变量占位，便于多环境部署

## 参考

- [Spring Cloud Config 官方文档](https://docs.spring.io/spring-cloud-config/docs/current/reference/html/) 
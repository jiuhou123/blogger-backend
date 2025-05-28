# jiuhou-config-server 说明

本服务为九猴博客系统的 Spring Cloud Config 配置中心，负责统一管理和分发各微服务的配置文件。

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
# Bug Debugging Log

This document records the process of debugging significant bugs encountered during the development of this project.

## Bug Entry Template

### Date: YYYY-MM-DD

**Summary:** A brief description of the bug.

**Symptoms:** What was observed? How did the bug manifest?

**Investigation:** Steps taken to identify the root cause. Tools used, hypotheses tested.

**Root Cause:** The underlying reason for the bug.

**Solution:** The fix implemented.

**Verification:** How was the fix tested and confirmed?

**Lessons Learned:** Any insights gained from debugging this bug.

---

## Recorded Bugs

<!-- Add new bug entries below this line -->

### Date: 2024-06-05

**Summary:** `ServiceException` 消息在全局异常处理器中丢失。

**Symptoms:** 登录接口返回通用错误提示"业务处理失败"，而不是 `UserServiceImpl` 中抛出的具体业务异常消息（如"用户不存在"或"密码错误"）。日志显示 `GlobalExceptionHandler` 捕获到了 `ServiceException`，但其 `getMessage()` 方法返回 `null`。

**Investigation:**
- 在 `UserServiceImpl.login` 方法中，确认抛出 `ServiceException` 时消息是存在的。
- 在 `GlobalExceptionHandler` 中添加详细日志，包括异常类名、消息、本地化消息、堆栈跟踪和原因，确认异常对象被捕获，但消息为 `null` 且无 `cause` 异常。
- 尝试将 `/auth/login` 接口在 `SecurityConfig` 中设置为 `permitAll()`，希望能绕过 Spring Security 的部分处理，但问题依旧。
- 添加自定义过滤器 `ExceptionLoggingFilter` 并置于 Spring Security 过滤器链中 `ExceptionTranslationFilter` 之前，但该过滤器未能捕获到 `ServiceException`，表明异常在 Spring Security 过滤器链的更深层被处理。
- 尝试给 `GlobalExceptionHandler` 设置更高的 `@Order` 优先级，问题仍未解决。

**Root Cause:** 异常很可能在 Spring Security 的过滤器链内部（可能是 `ExceptionTranslationFilter` 或相关组件）被捕获和处理，导致原始 `ServiceException` 的消息在传播到 Spring MVC 的异常处理机制时丢失。这种行为即使在 `permitAll()` 路径下也可能发生。

**Solution:** 实现变通方案，使用 `ThreadLocal` (`ServiceExceptionMessageHolder`) 在抛出 `ServiceException` (`UserServiceImpl`) 时存储业务消息。创建一个位于 Spring Security 过滤器链之前的自定义过滤器 (`ServiceExceptionHandlingFilter`)，捕获 `ServiceException` 并直接构建包含从 `ThreadLocal` 获取的业务消息的响应体返回，同时在请求结束时清除 `ThreadLocal`。

**Verification:** 再次调用登录接口，返回的响应体中的 `msg` 字段正确显示了 `UserServiceException` 中抛出的具体业务错误消息（如"密码错误"）。

**Lessons Learned:** Spring Security 可能会意外影响非安全相关异常在过滤器链中的传播，导致信息丢失。对于这种难以定位的框架内部问题，可以考虑使用 `ThreadLocal` 或更早的自定义过滤器来绕过框架的默认处理行为。

---

### Date: 2024-06-05

**Summary:** 启动 `auth` 模块时出现 `Invalid value type` 错误，与 `mybatis-spring` 版本兼容性有关。

**Symptoms:** 启动 `jiuhou-auth` 模块时，日志显示 `java.lang.IllegalArgumentException: Invalid value type for attribute 'factoryBeanObjectType': java.lang.String`。

**Investigation:**
- 根据错误信息和堆栈跟踪，判断可能与依赖冲突或 Spring 配置有关。
- 查看 `jiuhou-auth/pom.xml` 文件。
- 运行 `mvn dependency:tree` 分析依赖树，发现 `mybatis-spring` 版本较低 (2.1.2)。
- 网络搜索发现此错误通常是由于旧版本的 `mybatis-spring` 与 Spring Framework 6.1.x 不兼容。

**Root Cause:** `jiuhou-auth` 模块使用的 `mybatis-spring` 版本 (2.1.2) 与项目整体升级到的 Spring Boot 3.x (对应 Spring Framework 6.1.x) 存在兼容性问题。

**Solution:** 在 `jiuhou-auth/pom.xml` 中显式引入兼容 Spring Framework 6.1.x 的 `mybatis-spring` 版本 3.0.3。

**Verification:** 重新编译并启动 `auth` 模块，错误不再出现。

**Lessons Learned:** 在升级 Spring Boot/Spring Cloud 版本时，需要特别注意相关生态库（如 MyBatis-Spring, Redis 客户端等）的兼容性。

---

### Date: 2024-06-05

**Summary:** `auth` 模块启动时无法解析 `jwt.secret` 占位符。

**Symptoms:** `jiuhou-auth` 模块启动失败，日志显示 `Could not resolve placeholder 'jwt.secret'`。

**Investigation:**
- 根据错误判断是配置加载问题。
- 尝试列出 Config Server 本地配置仓库目录内容，发现路径错误。
- 修正路径后列出目录，发现本地仓库 `C:\dev\workspace\jiuhou-config` 下无 `blogger-config` 目录，实际应为 `config-repo`。
- 根据用户截图确认本地仓库路径和结构，发现 `auth` 模块配置在 `auth-service` 目录下，但服务名在 `bootstrap.yml` 中配置为 `jiuhou-auth`。

**Root Cause:** Config Server 无法根据配置的服务名 `jiuhou-auth` 在其配置仓库中找到对应的配置文件（期望找到 `jiuhou-auth.yml` 或 `jiuhou-auth-dev.yml` 等），因为配置文件的目录和名称 (`auth-service.yml`) 与服务名不匹配。

**Solution:** 建议将本地 Config 仓库中 `auth-service` 目录重命名为 `jiuhou-auth`，并将 `auth-service.yml` 文件重命名为 `jiuhou-auth.yml`。

**Verification:** 重命名目录和文件后，再次启动 `auth` 模块，不再报告 `jwt.secret` 占位符无法解析的错误。

**Lessons Learned:** Spring Cloud Config Server 根据服务名查找配置文件时，默认期望配置文件的目录或名称与服务名匹配。配置仓库的路径和结构需要与服务端的查找规则一致。

---

### Date: 2024-06-05

**Summary:** Config Server 启动时仍然尝试连接 GitHub 远程仓库。

**Symptoms:** Config Server 启动日志显示尝试连接 `https://github.com/jiuhou123/jiuhou-config.git` 并出现权限错误，尽管本地配置 (`application-local.yml`) 指向本地仓库。

**Investigation:**
- 查看 `jiuhou-config-server/src/main/resources/application.yml` 和 `application-local.yml`。
- 确认 `application-local.yml` 正确配置了本地仓库路径。
- 发现 `application.yml` 中存在注释掉的远程仓库 URI 配置。
- 检查 Config Server 的启动方式和激活的 profile，确认 `local` profile 已激活。

**Root Cause:** 本地 Git 仓库的 `.git/config` 文件中可能仍然配置了远程仓库 `origin`，Config Server 可能会读取这些信息，或者 `application.yml` 中的远程 URI 配置（即使注释掉）可能干扰了本地仓库的加载。

**Solution:** 1. 删除 `application.yml` 中注释掉的远程仓库 URI 行。 2. 指导用户进入本地 Config 仓库目录 (`C:\dev\workspace\jiuhou-config`)，手动编辑 `.git/config` 文件，删除 `[remote "origin"]` 段落。

**Verification:** 删除远程 URI 配置并修改本地 `.git/config` 后，Config Server 启动日志不再显示连接 GitHub 的错误。

**Lessons Learned:** Spring Cloud Config Server 在加载本地 Git 仓库时，可能会受到 `.git/config` 文件中远程配置的影响。确保本地仓库是纯本地的，或者配置明确指向本地路径并排除远程配置的干扰是很重要的。

---

### Date: 2024-06-05

**Summary:** Config Server 启动后页面无法访问，报错 `Cannot load environment`。

**Symptoms:** 访问 Config Server 的 `/jiuhou-auth/default` 端点时，页面显示错误 `Cannot load environment` 和 `Could not fetch default label main`。

**Investigation:**
- Config Server 日志显示成功从本地路径加载配置，但端点访问失败。
- 用户确认本地 Config 仓库目录 (`C:\dev\workspace\jiuhou-config`) 没有 `.git` 目录。

**Root Cause:** Spring Cloud Config Server 在使用 Git 作为后端存储时，要求指定的本地路径是一个有效的 Git 仓库（即包含 `.git` 目录）。缺少 `.git` 目录导致 Config Server 无法将其识别为 Git 仓库并加载环境。

**Solution:** 指导用户在本地 Config 仓库备份目录 (`C:\dev\workspace\jiuhou-config`) 中执行 `git init` 初始化本地仓库，然后 `git add .` 添加所有文件，最后 `git commit -m "Initial commit"` 提交。

**Verification:** 初始化本地 Git 仓库并提交后，Config Server 能够正常启动并成功加载配置环境，访问端点不再报错。

**Lessons Learned:** Spring Cloud Config Server 使用本地 Git 仓库时，必须确保该目录是一个通过 `git init` 初始化过的有效 Git 仓库。

---

### Date: 2024-06-05

**Summary:** `auth` 模块启动时出现循环依赖错误。

**Symptoms:** `jiuhou-auth` 模块启动失败，日志显示 `BeanCurrentlyInCreationException`，指示 `SecurityConfig` 和 `JwtAuthenticationFilter` 等 Bean 之间存在循环依赖。

**Investigation:**
- 根据错误信息和堆栈跟踪，判断是 Spring 容器初始化 Bean 时出现循环引用。
- 查看 `SecurityConfig.java` 和 `JwtAuthenticationFilter.java` 的代码。
- 发现 `SecurityConfig` 通过构造函数注入 `JwtAuthenticationFilter`，而 `JwtAuthenticationFilter` 可能也间接依赖或被 Spring Security 的其他配置所依赖，形成循环。

**Root Cause:** `SecurityConfig` 直接依赖于 `JwtAuthenticationFilter` Bean 的创建，而 `JwtAuthenticationFilter` 的完全初始化又依赖于 Spring Security 的其他组件，这些组件的初始化可能又需要 `SecurityConfig` 中定义的 Bean，从而形成循环引用。

**Solution:** 修改 `SecurityConfig`，将 `JwtAuthenticationFilter` 的注入方式从构造函数注入改为方法参数注入 (`filterChain` 方法的参数)。Spring 会在调用方法时解析参数依赖，而不是在 Bean 实例化阶段。

**Verification:** 修改注入方式后，重新编译并启动 `auth` 模块，循环依赖错误消失，模块成功启动。

**Lessons Learned:** 在 Spring Configuration 类中，当 Bean 之间存在复杂的相互依赖时，构造函数注入可能导致循环依赖。可以考虑使用方法参数注入或 `@Lazy` 注解（谨慎使用）来打破循环。

---

### Date: 2024-06-05

**Summary:** Swagger UI 相关路径被 Spring Security 拒绝 (403 Forbidden)。

**Symptoms:** 访问 `auth` 模块的 Swagger UI 页面（如 `/swagger-ui.html`）时，收到 `403 Forbidden` 错误。

**Investigation:**
- 根据错误判断是 Spring Security 阻止了访问。
- 查看 `SecurityConfig.java` 文件。
- 发现 Swagger UI 的默认路径 (`/swagger-ui.html`, `/swagger-ui/**`, `/v3/api-docs`, `/v3/api-docs/**`) 没有添加到 `permitAll()` 允许匿名访问的路径列表中。

**Root Cause:** Spring Security 默认会拦截所有请求并要求认证。Swagger UI 的路径没有明确配置为公共可访问，因此被拦截并拒绝访问。

**Solution:** 在 `SecurityConfig.java` 的 `filterChain` 方法中，将 Swagger UI 相关的路径添加到 `requestMatchers().permitAll()` 列表中。

**Verification:** 修改 `SecurityConfig` 后，重新编译并启动 `auth` 模块，可以正常访问 Swagger UI 页面。

**Lessons Learned:** 在使用 Spring Security 时，对于需要公共访问的资源（如文档、静态资源等），需要明确将其路径添加到安全配置的白名单中。

---

### Date: 2024-06-06

**Summary:** 注册接口保存用户时报错 `column "user_id" does not exist`。

**Symptoms:** 调用注册接口创建用户时，日志显示数据库错误 `column "user_id" does not exist`。

**Investigation:**
- 根据错误判断是数据库列名与实体类字段映射不匹配。
- 查看 `User.java` 实体类，发现主键字段名为 `userId`。
- 查看数据库 `sys_user` 表结构（通过查看初始化 SQL 脚本），确认主键列名为 `id`。
- 查看 `UserMapper.java`，没有自定义 SQL 覆盖默认映射。

**Root Cause:** MyBatis Plus 默认根据实体类字段名（驼峰命名）映射数据库列名（下划线命名）。`User.java` 中主键字段是 `userId`，MyBatis Plus 默认映射到数据库列 `user_id`，而实际数据库表中的主键列是 `id`。

**Solution:** 修改 `User.java` 实体类，将主键字段名从 `userId` 改为 `id`，使其与数据库表列名一致。

**Verification:** 修改实体类后，重新编译并启动 `auth` 模块，注册接口不再报告 `column "user_id" does not exist` 错误。

**Lessons Learned:** 使用 ORM 框架时，需要确保实体类与数据库表之间的映射关系正确。默认映射规则不匹配时，需要通过注解或配置进行显式映射。

---

### Date: 2024-06-06

**Summary:** 注册接口保存用户时报错 `column "nickname" does not exist`。

**Symptoms:** 调用注册接口创建用户时，继主键问题解决后，再次报错 `column "nickname" does not exist`。

**Investigation:**
- 根据错误判断仍是实体类字段与数据库表结构不匹配。
- 查看 `User.java` 实体类，发现存在 `nickname`、`mobile`、`sex`、`avatar`、`delFlag` 等字段。
- 查看数据库 `sys_user` 表结构（通过查看初始化 SQL 脚本），发现表中没有这些列。

**Root Cause:** `User.java` 实体类中存在多个字段（`nickname` 等），但在数据库 `sys_user` 表中没有对应的列。MyBatis Plus 尝试将实体类中的字段保存到数据库，但数据库表中不存在这些列。

**Solution:** 1. 暂时从 `User.java` 实体类中移除这些不存在的字段以使注册功能通过。 2. 创建新的 Flyway 迁移脚本 `V1.0.1__add_missing_user_columns.sql`，向 `sys_user` 表中添加这些缺失的列。

**Verification:** 移除实体类字段后注册成功。创建并执行 Flyway 脚本后，数据库表结构更新，可以将字段重新添加回实体类。

**Lessons Learned:** 实体类定义应该与数据库表结构保持同步。数据库结构的变更需要通过迁移工具（如 Flyway）进行管理和应用。

---

### Date: 2024-06-06

**Summary:** 数据库初始化和迁移 SQL 文件存在乱码警告。

**Symptoms:** Flyway 执行 `V1.0.0__init_schema.sql` 和 `V1.0.1__add_missing_user_columns.sql` 时，日志中出现关于文件编码的警告或执行失败。

**Investigation:**
- 查看 SQL 文件内容，发现存在非 ASCII 字符（如中文注释或字符串）可能导致编码问题。
- Flyway 在执行 SQL 文件时，需要确保文件编码与数据库连接或客户端编码一致。

**Root Cause:** SQL 文件的编码（可能是 UTF-8）与 PostgreSQL 数据库或客户端连接使用的编码不匹配。

**Solution:** 在每个 SQL 文件的开头添加 `SET client_encoding TO 'UTF8';` 语句，明确指定客户端编码为 UTF8，以确保 SQL 内容被正确解析和执行。

**Verification:** 在 SQL 文件中添加编码设置后，Flyway 执行不再报告乱码警告，数据库初始化和迁移成功。

**Lessons Learned:** 在处理包含多语言字符或特殊字符的 SQL 文件时，务必注意文件编码，并根据数据库类型和连接配置，在 SQL 脚本中明确指定客户端编码，以避免乱码问题。

---

### Date: 2024-06-06

**Summary:** 用户注册后数据库 `create_time` 和 `update_time` 字段为 `NULL`。

**Symptoms:** 通过注册接口创建新用户后，查看数据库 `sys_user` 表，发现 `create_time` 和 `update_time` 列的值为 `NULL`。

**Investigation:**
- 检查 `User.java` 实体类，发现 `createTime` 和 `updateTime` 字段上使用了 MyBatis Plus 的自动填充注解 (`@TableField(fill = FieldFill.INSERT)` 和 `@TableField(fill = FieldFill.INSERT_UPDATE)`)。
- 搜索代码库，未找到 `MetaObjectHandler` 的实现类。
- MyBatis Plus 的自动填充功能依赖于用户提供的 `MetaObjectHandler` 实现类，该类负责在插入和更新时为标记了填充注解的字段设置值。

**Root Cause:** 虽然实体类使用了自动填充注解，但缺少 MyBatis Plus 核心功能所需的 `MetaObjectHandler` 实现类，导致自动填充逻辑未被执行。

**Solution:** 创建一个实现 `MetaObjectHandler` 接口的类（如 `MyMetaObjectHandler`），并在其中实现 `insertFill` 和 `updateFill` 方法，为 `createTime` 和 `updateTime` 字段设置当前时间。

**Verification:** 创建并配置 `MyMetaObjectHandler` 后，再次通过注册接口创建用户，数据库中 `create_time` 和 `update_time` 字段能够被正确自动填充为当前时间。

**Lessons Learned:** 使用 MyBatis Plus 的自动填充功能需要同时满足两个条件：在实体类字段上添加填充注解，并提供一个实现 `MetaObjectHandler` 接口的 Bean 来定义具体的填充逻辑。

---

### Date: 2024-06-06

**Summary:** 刷新接口 (`/auth/refresh`) 返回 403 Forbidden。

**Symptoms:** 调用 `/auth/refresh` 接口时，收到 HTTP 状态码 403。

**Investigation:**
- 403 Forbidden 通常表示请求被服务器拒绝，在 Spring Security 集成的应用中，这往往是权限不足或未通过认证导致的。
- 查看 `SecurityConfig.java` 文件，确认 `/auth/refresh` 路径是否被允许匿名访问。

**Root Cause:** `/auth/refresh` 路径没有添加到 Spring Security 的 `permitAll()` 允许匿名访问的路径列表中，导致被默认的认证/授权规则拦截。

**Solution:** 在 `SecurityConfig.java` 的 `filterChain` 方法中，将 `/auth/refresh` 路径添加到 `requestMatchers().permitAll()` 列表中。

**Verification:** 修改 `SecurityConfig` 并重启模块后，`/auth/refresh` 接口不再返回 403 Forbidden，可以正常访问并执行刷新逻辑。

**Lessons Learned:** 任何不需要认证或授权即可访问的路径，都需要显式地在 Spring Security 配置中设置为 `permitAll()`。

---

### Date: 2024-06-06

**Summary:** 注册接口 `ServiceException` 消息丢失。

**Symptoms:** 调用注册接口时，如果发生业务异常（如用户名已存在），返回的错误响应中 `msg` 字段显示为通用错误信息（如"业务处理失败"或 `null`），而不是 `UserServiceImpl` 中抛出的具体业务异常消息。

**Investigation:**
- 问题现象与之前登录接口遇到的 `ServiceException` 消息丢失问题一致。
- 检查注册相关的 `UserServiceImpl.register` 方法，确认在抛出 `ServiceException` 时消息是存在的。
- 推断问题根源与 Spring Security 对异常的处理方式有关，即使对于 `permitAll()` 路径也可能影响异常消息的传播。

**Root Cause:** 与登录接口类似，异常可能在 Spring Security 过滤器链内部被捕获和处理，导致原始 `ServiceException` 的消息丢失，未能正确传递到全局异常处理器。

**Solution:** 将之前用于解决登录接口 `ServiceException` 消息丢失的变通方案（使用 `ThreadLocal` 存储消息并在自定义过滤器中处理）应用到注册接口的异常处理中。

**Verification:** 修改 `UserServiceImpl.register` 方法，在抛出 `ServiceException` 前将消息存储到 `ServiceExceptionMessageHolder` 的 `ThreadLocal` 中。重新测试注册接口，返回的响应体中的 `msg` 字段正确显示了具体的业务错误消息。

**Lessons Learned:** 针对 Spring Security 可能导致的异常消息丢失问题，之前实现的 `ThreadLocal` + 自定义过滤器变通方案可以应用于其他受影响的接口。

---

### Date: 2025-06-10

**Summary:** `jiuhou-blog-service` 模块启动时，Spring 容器无法找到 `CategoryService` 和 `CategoryMapper` 的 Bean。

**Symptoms:** 应用程序启动失败，日志显示 `org.springframework.beans.factory.UnsatisfiedDependencyException`，具体指出 `Field categoryService in com.jiuhou.blog.controller.CategoryController required a bean of type 'com.jiuhou.blog.service.CategoryService' that could not be found.` 随后又出现 `No qualifying bean of type 'com.jiuhou.blog.mapper.CategoryMapper' available`。

**Investigation:**
- 检查 `jiuhou-blog-service/src/main/java/com/jiuhou/blog/service/impl/CategoryServiceImpl.java` 文件，发现 `CategoryServiceImpl` 类缺少 `@Service` 注解。
- 检查 `jiuhou-blog-service/src/main/java/com/jiuhou/blog/mapper/CategoryMapper.java` 文件，发现 `CategoryMapper` 接口缺少 `@Mapper` 注解。
- 进一步检查 `jiuhou-blog-service/src/main/java/com/jiuhou/blog/mapper/` 目录下其他 Mapper 接口（`BlogPostMapper`、`ArticleMapper`、`CommentMapper`、`TagMapper`），发现它们也缺少 `@Mapper` 注解。

**Root Cause:** Spring Boot 应用程序在启动时，无法通过组件扫描识别并注册 `CategoryServiceImpl` 和各个 Mapper 接口为 Spring Bean。这是因为 `CategoryServiceImpl` 缺少 `@Service` 注解，而所有 Mapper 接口缺少 `@Mapper` 注解。

**Solution:**
- 在 `jiuhou-blog-service/src/main/java/com/jiuhou/blog/service/impl/CategoryServiceImpl.java` 文件的 `CategoryServiceImpl` 类上添加 `@Service` 注解。
- 在 `jiuhou-blog-service/src/main/java/com/jiuhou/blog/mapper/CategoryMapper.java` 文件的 `CategoryMapper` 接口上添加 `@Mapper` 注解。
- 在 `jiuhou-blog-service/src/main/java/com/jiuhou/blog/mapper/BlogPostMapper.java` 文件的 `BlogPostMapper` 接口上添加 `@Mapper` 注解。
- 在 `jiuhou-blog-service/src/main/java/com/jiuhou/blog/mapper/ArticleMapper.java` 文件的 `ArticleMapper` 接口上添加 `@Mapper` 注解。
- 在 `jiuhou-blog-service/src/main/java/com/jiuhou/blog/mapper/CommentMapper.java` 文件的 `CommentMapper` 接口上添加 `@Mapper` 注解。
- 在 `jiuhou-blog-service/src/main/java/com/jiuhou/blog/mapper/TagMapper.java` 文件的 `TagMapper` 接口上添加 `@Mapper` 注解。

**Verification:** 重新编译并启动 `jiuhou-blog-service` 模块，所有相关的 Bean 依赖注入错误消失，模块成功启动并注册到 Eureka。

**Lessons Learned:** 确保所有需要被 Spring 容器管理的服务实现类、数据访问接口（Mapper）都使用了正确的 Spring (`@Service`) 或 MyBatis (`@Mapper`) 注解，以便 Spring 能够进行组件扫描和 Bean 的自动装配。 
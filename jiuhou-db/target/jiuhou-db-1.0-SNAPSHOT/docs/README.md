# 久候博客系统 - 数据库模块

本模块负责数据库初始化和迁移管理。使用 PostgreSQL 作为数据库，Flyway 作为数据库版本控制工具。

## 环境要求

- JDK 17 或以上
- Maven 3.8 或以上
- PostgreSQL 15 或以上
- Linux/Unix 操作系统

## 目录结构

```
jiuhou-db/
├── config/                    # 配置文件目录
│   └── db.conf               # 数据库配置文件
├── scripts/                   # 脚本目录
│   ├── init-db.sh           # 数据库初始化脚本
│   └── execute-sql.sh       # SQL执行脚本
├── sql/                      # SQL迁移文件目录
│   └── V1.0.0__init_schema.sql  # 初始化表结构
└── flyway.conf               # Flyway配置文件
```

## 快速开始

### 1. 环境准备

1. 确保已安装 PostgreSQL：
   ```bash
   # Ubuntu/Debian
   sudo apt-get install postgresql postgresql-client

   # CentOS/RHEL
   sudo yum install postgresql postgresql-server
   ```

2. 确保 PostgreSQL 服务已启动：
   ```bash
   # 使用 systemctl
   sudo systemctl start postgresql

   # 或使用 service
   sudo service postgresql start
   ```

### 2. 配置数据库

1. 修改 `config/db.conf` 文件，设置数据库连接信息：
   ```properties
   DB_HOST=localhost
   DB_PORT=5432
   DB_NAME=jiuhou_blog
   DB_USER=postgres
   DB_PASSWORD=your_password
   ```

### 3. 打包部署

1. 在项目根目录执行打包命令：
   ```bash
   mvn clean package
   ```

2. 打包后的文件位于：
   ```
   target/jiuhou-db-1.0-SNAPSHOT-package/jiuhou-db-1.0-SNAPSHOT/
   ```

### 4. 初始化数据库

有两种方式可以初始化数据库：

#### 方式一：使用 Shell 脚本（推荐）

1. 进入部署目录：
   ```bash
   cd target/jiuhou-db-1.0-SNAPSHOT-package/jiuhou-db-1.0-SNAPSHOT/
   ```

2. 添加执行权限：
   ```bash
   chmod +x bin/*.sh
   ```

3. 执行初始化脚本：
   ```bash
   ./bin/init-db.sh
   ```
   
4. 执行 SQL 脚本：
   ```bash
   ./bin/execute-sql.sh
   ```

#### 方式二：使用 Flyway 命令

1. 进入部署目录：
   ```bash
   cd target/jiuhou-db-1.0-SNAPSHOT-package/jiuhou-db-1.0-SNAPSHOT/
   ```

2. 修改 `flyway.conf` 文件，设置数据库连接信息：
   ```properties
   flyway.url=jdbc:postgresql://localhost:5432/jiuhou_blog
   flyway.user=postgres
   flyway.password=your_password
   flyway.locations=filesystem:sql
   flyway.baselineOnMigrate=true
   flyway.encoding=UTF-8
   ```

3. 执行 Flyway 迁移：
   ```bash
   # 如果已安装了 Flyway CLI
   flyway migrate

   # 或者使用 Maven（在项目根目录下执行）
   cd ../../../
   mvn flyway:migrate
   ```

## 常见问题

1. **找不到 psql 命令**
   - 确保 PostgreSQL 已正确安装
   - 检查是否已安装 postgresql-client 包

2. **数据库连接失败**
   - 检查 PostgreSQL 服务是否启动
   - 验证数据库用户名和密码是否正确
   - 确认数据库端口是否被占用或防火墙是否放行
   - 检查 pg_hba.conf 配置是否允许连接

3. **SQL 执行失败**
   - 检查 SQL 文件语法是否正确
   - 确认数据库用户是否有足够权限
   - 查看 PostgreSQL 日志获取详细错误信息：
     ```bash
     sudo tail -f /var/log/postgresql/postgresql-*.log
     ```

## 版本历史

- v1.0.0
  - 初始化项目结构
  - 添加基础表结构
  - 实现数据库初始化脚本

## 维护者

- 九侯开发团队

## 许可证

[MIT](LICENSE) 
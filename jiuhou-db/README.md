# jiuhou-db 数据库模块

## 简介
本模块用于博客系统的数据库初始化、升级和卸载，支持一键化安装和清理，适用于 PostgreSQL。

## 环境要求

- JDK 17 或以上
- Maven 3.8 或以上
- PostgreSQL 15 或以上
- Linux/Unix 操作系统

## 目录结构

```
jiuhou-db-1.0-SNAPSHOT/
├── bin/           # 脚本目录（init-db.sh、install.sh、uninstall-db.sh）
├── config/        # 数据库配置文件
├── docs/          # 操作文档
├── sql/           # SQL 脚本（自动打包 src/main/resources/db/migration 下所有 .sql 文件）
└── pom.xml
```

## 操作步骤

### 1. 解压产物包并进入目录

```bash
unzip target/jiuhou-db-1.0-SNAPSHOT-package/jiuhou-db-1.0-SNAPSHOT.zip -d /your/deploy/path/
cd /your/deploy/path/jiuhou-db-1.0-SNAPSHOT
```

### 2. 配置数据库连接

编辑 `config/db.conf`，填写数据库主机、端口、库名、用户名和密码。

### 3. 赋予脚本执行权限（Linux/WSL）

```bash
chmod +x bin/*.sh
```

### 4. 初始化数据库（全新安装）

```bash
sh bin/install.sh install
```
- 创建数据库（如不存在）
- 批量执行所有 SQL 脚本，初始化表结构
- 适用于首次部署

### 5. 升级数据库（仅执行 SQL 脚本，不新建库）

```bash
sh bin/install.sh upgrade
```
- 不新建数据库，仅批量执行 SQL 脚本
- 适用于已有数据库结构升级

### 6. 卸载数据库（清空所有表，危险操作！）

```bash
sh bin/install.sh uninstall
```
- 会调用 `bin/uninstall-db.sh`，清空当前库下所有表
- **操作前请务必确认数据库连接配置，避免误删数据！**
- 过程中会有二次确认提示

### 7. 常见问题
- SQL 文件必须为 UTF-8 无 BOM 编码，否则可能报错
- Windows 下如遇 target 目录无法删除，需手动关闭占用进程或重启
- 卸载操作仅清空表，不会删除数据库本身

## 其他说明
- 所有脚本均为 bash 脚本，建议在 Git Bash、WSL 或 Linux 环境下运行
- 如需自定义表结构，请编辑 `sql/` 目录下的 SQL 文件

---
如有更多问题请查阅 docs 目录或联系开发者。

## 版本历史

- v1.0.0
  - 初始化项目结构
  - 添加基础表结构
  - 实现数据库初始化脚本

## 维护者

-久候开发团队

## 许可证

[MIT](LICENSE)

## 打包与部署

### 1. 打包

在 `jiuhou-db` 目录下执行：

```bash
mvn clean package
```

- 打包成功后，会在 `target/` 目录下生成 zip 和 tar.gz 格式的安装包，如：
  ```
  target/jiuhou-db-1.0-SNAPSHOT-package/jiuhou-db-1.0-SNAPSHOT.zip
  ```

### 2. 解压产物包

将产物包（如 zip 文件）解压到任意部署目录，例如：

```bash
unzip target/jiuhou-db-1.0-SNAPSHOT-package/jiuhou-db-1.0-SNAPSHOT.zip -d /your/deploy/path/
cd /your/deploy/path/jiuhou-db-1.0-SNAPSHOT
```

### 3. 产物包目录结构

解压后目录结构如下：

```
jiuhou-db-1.0-SNAPSHOT/
├── bin/           # 脚本目录（init-db.sh、install.sh、uninstall-db.sh）
├── config/        # 数据库配置文件
├── docs/          # 操作文档
├── sql/           # SQL 脚本（自动打包 src/main/resources/db/migration 下所有 .sql 文件）
└── pom.xml
```

### 4. 配置与执行

1. **配置数据库连接**
   - 编辑 `config/db.conf`，填写数据库连接信息。

2. **赋予脚本执行权限（Linux/WSL）**
   ```bash
   chmod +x bin/*.sh
   ```

3. **执行安装/升级/卸载**
   ```bash
   # 全新安装
   sh bin/install.sh install

   # 升级
   sh bin/install.sh upgrade

   # 卸载（清空所有表，危险操作！）
   sh bin/install.sh uninstall
   ```

### 5. 注意事项

- **所有操作建议在解压后的产物包目录下进行**，不要直接在源码目录操作。
- **SQL 文件自动打包自 `src/main/resources/db/migration`，如需自定义请提前放好。**
- **Windows 下如遇脚本权限或路径问题，建议用 Git Bash 或 WSL 执行。**
- **如遇 target 目录无法删除，需手动关闭占用进程或重启。** 
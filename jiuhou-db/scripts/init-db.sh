#!/bin/bash

# 脚本所在目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# 加载配置
CONFIG_FILE="$SCRIPT_DIR/../config/db.conf"
if [ ! -f "$CONFIG_FILE" ]; then
    echo "错误: 配置文件不存在: $CONFIG_FILE"
    exit 1
fi
source "$CONFIG_FILE"

# 检查必要的环境变量
required_vars=("DB_HOST" "DB_PORT" "DB_NAME" "DB_USER" "DB_PASSWORD")
for var in "${required_vars[@]}"; do
    if [ -z "${!var}" ]; then
        echo "错误: 环境变量 $var 未设置"
        exit 1
    fi
done

# 检查 psql 命令是否可用
if ! command -v psql &> /dev/null; then
    echo "错误: 未找到 psql 命令，请确保已安装 PostgreSQL 客户端工具"
    echo "Windows 用户请确保 PostgreSQL 的 bin 目录已添加到 PATH 环境变量中"
    echo "通常路径为: C:\\Program Files\\PostgreSQL\\{版本}\\bin"
    exit 1
fi

# 是否需要新建数据库
if [ "$1" = "create-db" ]; then
    echo "正在创建数据库 ${DB_NAME}..."
    export PGPASSWORD="$DB_PASSWORD"
    psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d postgres -c "CREATE DATABASE ${DB_NAME} WITH ENCODING 'UTF8';"
    if [ $? -ne 0 ]; then
        echo "警告: 创建数据库失败，可能已存在，忽略此错误继续..."
    else
        echo "数据库创建成功！"
    fi
fi

echo "正在执行 SQL 文件..."
SQL_DIR="$SCRIPT_DIR/../sql"
if [ ! -d "$SQL_DIR" ]; then
    echo "错误: SQL 脚本目录不存在: $SQL_DIR"
    exit 1
fi

# 强制将所有 SQL 文件尝试从 GBK 转为 UTF-8，iconv 成功则替换原文件，失败则保留原文件
# for sql_file in "$SQL_DIR"/*.sql; do
#     iconv -f GBK -t UTF-8 "$sql_file" -o "$sql_file.utf8" 2>/dev/null
#     if [ $? -eq 0 ]; then
#         echo "已将 $sql_file 转为 UTF-8 编码"
#         mv "$sql_file.utf8" "$sql_file"
#     else
#         rm -f "$sql_file.utf8"
#     fi
# done

export PGPASSWORD="$DB_PASSWORD"
for sql_file in "$SQL_DIR"/*.sql; do
    echo "执行: $sql_file"
    psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -f "$sql_file" -v ON_ERROR_STOP=1
    if [ $? -ne 0 ]; then
        echo "错误: SQL 文件执行失败: $sql_file"
        # 移除 exit 1，继续执行下一个文件
    fi
done

echo "数据库初始化/升级完成！" 
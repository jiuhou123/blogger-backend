#!/bin/bash

# 数据库卸载脚本：清理所有表（危险操作！）
# 请确保 config/db.conf 已正确配置数据库连接信息

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
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

read -p "警告：此操作将清空数据库 ${DB_NAME} 下所有表，是否继续？(y/N): " confirm
if [[ ! "$confirm" =~ ^[Yy]$ ]]; then
    echo "操作已取消。"
    exit 0
fi

export PGPASSWORD="$DB_PASSWORD"
echo "正在清空数据库 ${DB_NAME} 下所有表..."

# 生成所有 DROP TABLE 语句到临时文件
# 注意 SQL 拼接语法，确保输出为：DROP TABLE IF EXISTS "表名" CASCADE;
tmp_sql="/tmp/drop_all_tables_$$.sql"
psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -Atc \
  "SELECT 'DROP TABLE IF EXISTS \"' || tablename || '\" CASCADE;' FROM pg_tables WHERE schemaname = 'public';" > "$tmp_sql"

# 执行临时 SQL 文件
psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -f "$tmp_sql"
ret=$?
rm -f "$tmp_sql"

if [ $ret -ne 0 ]; then
    echo "错误: 数据库清理失败"
    exit 1
fi

echo "数据库 ${DB_NAME} 已清空！" 
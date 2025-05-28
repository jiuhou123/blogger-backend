#!/bin/bash

# 一键安装脚本：初始化数据库并执行所有 SQL 脚本

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}" )" && pwd)"

show_help() {
    echo "用法: $0 [install|uninstall|help]"
    echo "  install   : 全新安装（建库+批量执行 SQL 脚本）"
    echo "  uninstall : 卸载数据库（清理所有表，需实现 uninstall-db.sh）"
    echo "  help      : 显示本帮助"
    echo
    echo "示例:"
    echo "  $0 install     # 全新安装（建库+批量执行 SQL）"
    echo "  $0 uninstall   # 卸载数据库"
    echo "  $0 help        # 查看帮助"
}

case "$1" in
    install)
        echo "[全新安装] 建库+批量执行 SQL 脚本"
        $SCRIPT_DIR/init-db.sh create-db || exit 1
        echo "全新安装完成！"
        ;;
    uninstall)
        echo "[卸载] 清理数据库"
        if [ -f "$SCRIPT_DIR/uninstall-db.sh" ]; then
            $SCRIPT_DIR/uninstall-db.sh || exit 1
            echo "数据库已卸载！"
        else
            echo "未找到卸载脚本 uninstall-db.sh，请自行实现数据库清理逻辑。"
            exit 1
        fi
        ;;
    help|--help|-h|"")
        show_help
        ;;
    *)
        echo "未知参数: $1"
        show_help
        exit 1
        ;;
esac 
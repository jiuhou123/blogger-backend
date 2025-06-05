package com.jiuhou.auth.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MyBatis Plus 元对象处理器，用于自动填充字段
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ...");
        // 填充创建时间和更新时间
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        // TODO: 如果需要自动填充创建人（createBy），可以在这里添加逻辑
        // this.strictInsertFill(metaObject, "createBy", String.class, "admin"); //
        // 示例，实际中应获取当前登录用户
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ...");
        // 填充更新时间
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        // TODO: 如果需要自动填充更新人（updateBy），可以在这里添加逻辑
        // this.strictUpdateFill(metaObject, "updateBy", String.class, "admin"); //
        // 示例，实际中应获取当前登录用户
    }
}
package com.jiuhou.auth.annotation;

import java.lang.annotation.*;

/**
 * 权限注解
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequirePermission {
    /**
     * 权限标识
     */
    String value();
}
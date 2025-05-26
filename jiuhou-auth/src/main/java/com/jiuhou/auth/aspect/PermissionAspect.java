package com.jiuhou.auth.aspect;

import com.jiuhou.auth.annotation.RequirePermission;
import com.jiuhou.auth.domain.User;
import com.jiuhou.common.exception.ServiceException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * 权限切面
 */
@Aspect
@Component
public class PermissionAspect {

    @Before("@annotation(com.jiuhou.auth.annotation.RequirePermission)")
    public void checkPermission(JoinPoint point) {
        // 获取注解
        MethodSignature signature = (MethodSignature) point.getSignature();
        RequirePermission requirePermission = signature.getMethod().getAnnotation(RequirePermission.class);
        String permission = requirePermission.value();

        // 获取当前用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ServiceException("未登录");
        }

        // 获取用户信息
        User user = (User) authentication.getPrincipal();
        // TODO: 实现具体的权限校验逻辑
        // 这里可以根据实际需求，从数据库中查询用户的权限列表，然后进行校验
        if (!hasPermission(user, permission)) {
            throw new ServiceException("权限不足");
        }
    }

    /**
     * 判断用户是否有权限
     * 这里需要根据实际业务实现具体的权限判断逻辑
     */
    private boolean hasPermission(User user, String permission) {
        // TODO: 实现具体的权限判断逻辑
        return true;
    }
}
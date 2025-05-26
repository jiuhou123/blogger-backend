package com.jiuhou.auth.handler;

import com.jiuhou.common.domain.R;
import com.jiuhou.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务异常
     */
    @ExceptionHandler(ServiceException.class)
    public R<Void> handleServiceException(ServiceException e) {
        log.error(e.getMessage(), e);
        return R.fail(e.getMessage());
    }

    /**
     * 认证失败
     */
    @ExceptionHandler(AuthenticationException.class)
    public R<Void> handleAuthenticationException(AuthenticationException e) {
        log.error(e.getMessage(), e);
        return R.fail("认证失败");
    }

    /**
     * 密码错误
     */
    @ExceptionHandler(BadCredentialsException.class)
    public R<Void> handleBadCredentialsException(BadCredentialsException e) {
        log.error(e.getMessage(), e);
        return R.fail("用户名或密码错误");
    }

    /**
     * 权限不足
     */
    @ExceptionHandler(AccessDeniedException.class)
    public R<Void> handleAccessDeniedException(AccessDeniedException e) {
        log.error(e.getMessage(), e);
        return R.fail("权限不足");
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return R.fail("系统异常");
    }
}
package com.jiuhou.auth.handler;

import com.jiuhou.common.domain.R;
import com.jiuhou.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.jiuhou.auth.utils.ServiceExceptionMessageHolder;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
// 添加 Order 注解，设置高优先级
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler implements Ordered {

    /**
     * 业务异常
     */
    @ExceptionHandler(ServiceException.class)
    public R<Void> handleServiceException(ServiceException e) {
        log.error("捕获到 ServiceException: 类名={}, 异常对象是否为null={}, 消息={}, 本地化消息={}",
                e.getClass().getName(), e == null, e != null ? e.getMessage() : null,
                e != null ? e.getLocalizedMessage() : null, e);
        // 打印更多异常信息
        log.error("ServiceException 堆栈跟踪:", e);
        if (e != null && e.getCause() != null) {
            log.error("ServiceException 原因:", e.getCause());
        }

        // 尝试获取异常消息，优先使用异常对象的消息，如果为null则尝试从 ThreadLocal 获取
        String errorMessage = (e != null && e.getMessage() != null) ? e.getMessage()
                : ServiceExceptionMessageHolder.getMessage();

        // 如果消息仍然是 null，使用通用提示
        if (errorMessage == null) {
            errorMessage = "业务处理异常 [" + (e != null ? e.getClass().getName() : "未知") + "]";
        }

        // 如果消息仍然是通用提示，且有堆栈跟踪，尝试从堆栈中提取一些信息
        // 注意：只有当原始异常消息为 null 且从 ThreadLocal 获取的消息也为 null 时，才添加堆栈信息
        if (e != null && (e.getMessage() == null && ServiceExceptionMessageHolder.getMessage() == null)
                && e.getStackTrace() != null && e.getStackTrace().length > 0) {
            errorMessage += " at " + e.getStackTrace()[0].getClassName() + "." + e.getStackTrace()[0].getMethodName()
                    + "(" + e.getStackTrace()[0].getLineNumber() + ")";
        }

        return R.fail(errorMessage);
    }

    /**
     * 认证失败
     */
    @ExceptionHandler(AuthenticationException.class)
    public R<Void> handleAuthenticationException(AuthenticationException e) {
        log.error(e.getMessage(), e);
        // 打印更多异常信息
        log.error("AuthenticationException 堆栈跟踪:", e);
        if (e.getCause() != null) {
            log.error("AuthenticationException 原因:", e.getCause());
        }
        return R.fail("认证失败");
    }

    /**
     * 密码错误
     */
    @ExceptionHandler(BadCredentialsException.class)
    public R<Void> handleBadCredentialsException(BadCredentialsException e) {
        log.error(e.getMessage(), e);
        // 打印更多异常信息
        log.error("BadCredentialsException 堆栈跟踪:", e);
        if (e.getCause() != null) {
            log.error("BadCredentialsException 原因:", e.getCause());
        }
        // BadCredentialsException 通常已经包含足够的信息，可以直接返回
        String errorMessage = (e != null && e.getMessage() != null) ? e.getMessage() : "用户名或密码错误";
        return R.fail(errorMessage);
    }

    /**
     * 权限不足
     */
    @ExceptionHandler(AccessDeniedException.class)
    public R<Void> handleAccessDeniedException(AccessDeniedException e) {
        log.error(e.getMessage(), e);
        // 打印更多异常信息
        log.error("AccessDeniedException 堆栈跟踪:", e);
        if (e.getCause() != null) {
            log.error("AccessDeniedException 原因:", e.getCause());
        }
        return R.fail("权限不足");
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception e) {
        log.error(e.getMessage(), e);
        // 打印更多异常信息
        log.error("系统异常堆栈跟踪:", e);
        if (e.getCause() != null) {
            log.error("系统异常原因:", e.getCause());
        }
        return R.fail("系统异常");
    }

    // 实现 getOrder 方法
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
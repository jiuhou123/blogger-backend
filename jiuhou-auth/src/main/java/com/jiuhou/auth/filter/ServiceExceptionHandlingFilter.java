package com.jiuhou.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiuhou.auth.utils.ServiceExceptionMessageHolder;
import com.jiuhou.common.domain.R;
import com.jiuhou.common.exception.ServiceException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1) // 确保在 Spring Security 过滤器链之前
@Slf4j
public class ServiceExceptionHandlingFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ServiceException e) {
            log.error("ServiceExceptionHandlingFilter 捕获到 ServiceException: 消息={}", e.getMessage(), e);
            handleServiceException(response, e);
        } catch (Throwable e) {
            // 对于其他异常，继续向上抛出，让 Spring Security 或 GlobalExceptionHandler 处理
            throw e;
        } finally {
            // 清除 ThreadLocal
            ServiceExceptionMessageHolder.clear();
        }
    }

    private void handleServiceException(HttpServletResponse response, ServiceException e) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK); // 通常业务异常返回200 OK，错误信息在响应体中
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        // 构建错误响应体，从异常对象或 ThreadLocal 获取消息
        String errorMessage = (e != null && e.getMessage() != null) ? e.getMessage()
                : ServiceExceptionMessageHolder.getMessage();
        if (errorMessage == null) {
            errorMessage = "未知业务异常"; // 最终的 fallback
        }

        R<Void> errorResponse = R.fail(errorMessage);

        // 将错误响应写入 HttpServletResponse
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}
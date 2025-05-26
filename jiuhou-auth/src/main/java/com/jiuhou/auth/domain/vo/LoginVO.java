package com.jiuhou.auth.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录响应对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginVO {

    /**
     * 访问token
     */
    private String token;

    /**
     * 刷新token
     */
    private String refreshToken;

    /**
     * 用户信息
     */
    private UserVO user;
}
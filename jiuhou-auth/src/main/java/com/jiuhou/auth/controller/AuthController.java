package com.jiuhou.auth.controller;

import com.jiuhou.auth.domain.User;
import com.jiuhou.auth.domain.dto.LoginDTO;
import com.jiuhou.auth.domain.vo.LoginVO;
import com.jiuhou.auth.service.UserService;
import com.jiuhou.auth.utils.JwtUtils;
import com.jiuhou.common.domain.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtils jwtUtils;

    /**
     * 登录
     */
    @PostMapping("/login")
    public R<LoginVO> login(@RequestBody LoginDTO loginDTO) {
        return R.ok(userService.login(loginDTO));
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public R<Boolean> register(@RequestBody User user) {
        return R.ok(userService.register(user));
    }

    /**
     * 刷新令牌
     */
    @PostMapping("/refresh")
    public R<LoginVO> refresh(@RequestHeader("Authorization") String token) {
        // 验证刷新令牌
        if (!jwtUtils.validateToken(token)) {
            return R.fail("刷新令牌已过期");
        }

        // 获取用户信息
        Long userId = jwtUtils.getUserIdFromToken(token);
        User user = userService.getById(userId);
        if (user == null || "1".equals(user.getStatus())) {
            return R.fail("用户不存在或已停用");
        }

        // 生成新的访问令牌和刷新令牌
        String newToken = jwtUtils.generateToken(userId);
        String newRefreshToken = jwtUtils.generateRefreshToken(userId);

        return R.ok(new LoginVO(newToken, newRefreshToken, null));
    }
}
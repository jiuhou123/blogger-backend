package com.jiuhou.auth.controller;

import com.jiuhou.auth.domain.User;
import com.jiuhou.auth.domain.dto.LoginDTO;
import com.jiuhou.auth.domain.vo.LoginVO;
import com.jiuhou.auth.domain.vo.UserVO;
import com.jiuhou.auth.dto.UserRegisterRequest;
import com.jiuhou.auth.service.UserService;
import com.jiuhou.auth.utils.JwtUtils;
import com.jiuhou.common.domain.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.jiuhou.auth.dto.RefreshTokenRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "认证接口", description = "用户认证和授权相关的接口")
public class AuthController {

    private final UserService userService;
    private final JwtUtils jwtUtils;

    /**
     * 登录
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "用户登录请求体", required = true, content = @Content(examples = @ExampleObject(value = "{\"username\":\"testuser\", \"password\":\"password123\"}")))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "登录成功", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"code\": 200, \"msg\": \"操作成功\", \"data\": {\"token\": \"ey...\", \"refreshToken\": \"ey...\", \"user\": { ...用户信息... }}}"))),
            @ApiResponse(responseCode = "401", description = "认证失败", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"code\": 401, \"msg\": \"认证失败\"}"))),
            @ApiResponse(responseCode = "500", description = "操作失败", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"code\": 500, \"msg\": \"用户名或密码错误\"}")))
    })
    public R<LoginVO> login(@RequestBody LoginDTO loginDTO) {
        return R.ok(userService.login(loginDTO));
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "用户注册请求体", required = true, content = @Content(examples = @ExampleObject(value = "{\"username\":\"testuser\", \"password\":\"password123\", \"email\":\"testuser@example.com\", \"nickname\":\"测试用户\", \"mobile\":\"13888888888\", \"sex\":\"0\", \"avatar\":\"http://example.com/avatar.png\"}")))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "注册成功", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"code\": 200, \"msg\": \"操作成功\", \"data\": true}"))),
            @ApiResponse(responseCode = "500", description = "操作失败", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"code\": 500, \"msg\": \"用户名已存在\"}")))
    })
    public R<Boolean> register(@RequestBody UserRegisterRequest registerRequest) {
        return R.ok(userService.register(registerRequest));
    }

    /**
     * 刷新令牌
     */
    @PostMapping("/refresh")
    @Operation(summary = "刷新用户令牌")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "刷新令牌请求体", required = true, content = @Content(examples = @ExampleObject(value = "{\"refreshToken\":\"ey...\"}")))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "刷新成功", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"code\": 200, \"msg\": \"操作成功\", \"data\": {\"token\": \"ey...\", \"refreshToken\": \"ey...\", \"user\": { ...用户信息... }}}"))),
            @ApiResponse(responseCode = "401", description = "刷新失败", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"code\": 401, \"msg\": \"刷新令牌无效或已过期\"}")))
    })
    public R<LoginVO> refresh(@RequestBody RefreshTokenRequest request) {
        LoginVO loginVO = userService.refreshToken(request.getRefreshToken());
        return R.ok(loginVO);
    }

    /**
     * 登出
     */
    @PostMapping("/logout")
    @Operation(summary = "用户登出")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "登出成功", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"code\": 200, \"msg\": \"操作成功\", \"data\": true}")))
    })
    public R<Boolean> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            userService.logout(user.getId());
            return R.ok(true);
        }
        // 如果获取不到用户信息，也认为是登出成功或者无需处理
        return R.ok(false);
    }
}
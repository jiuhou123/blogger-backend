package com.jiuhou.auth.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class RefreshTokenRequest {
    @NotBlank(message = "刷新令牌不能为空")
    private String refreshToken;
}
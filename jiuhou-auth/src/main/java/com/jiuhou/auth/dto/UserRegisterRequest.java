package com.jiuhou.auth.dto;

import lombok.Data;

@Data
public class UserRegisterRequest {
    private String username;
    private String password;
    private String email;
    private String nickname;
    private String mobile;
    private String sex;
    private String avatar;
    // 可以根据需要添加其他字段
}
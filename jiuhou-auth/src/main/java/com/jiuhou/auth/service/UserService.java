package com.jiuhou.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiuhou.auth.domain.User;
import com.jiuhou.auth.domain.dto.LoginDTO;
import com.jiuhou.auth.domain.vo.LoginVO;
import com.jiuhou.auth.dto.UserRegisterRequest;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {

    /**
     * 登录
     *
     * @param loginDTO 登录信息
     * @return 登录结果
     */
    LoginVO login(LoginDTO loginDTO);

    /**
     * 注册
     *
     * @param registerRequest 用户注册信息
     * @return 注册结果
     */
    boolean register(UserRegisterRequest registerRequest);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    User getByUsername(String username);

    /**
     * 根据邮箱查询用户
     *
     * @param email 邮箱
     * @return 用户信息
     */
    User getByEmail(String email);

    /**
     * 修改密码
     *
     * @param userId      用户ID
     * @param oldPassword 原密码
     * @param newPassword 新密码
     * @return 是否成功
     */
    boolean updatePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 重置密码
     *
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean resetPassword(Long userId);

    /**
     * 刷新用户令牌
     * 
     * @param refreshToken 刷新令牌
     * @return 新的令牌对和用户信息
     */
    LoginVO refreshToken(String refreshToken);

    /**
     * 用户登出
     *
     * @param userId 用户ID
     */
    void logout(Long userId);
}
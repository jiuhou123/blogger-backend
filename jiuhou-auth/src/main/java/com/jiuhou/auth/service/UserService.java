package com.jiuhou.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiuhou.auth.domain.User;
import com.jiuhou.auth.domain.dto.LoginDTO;
import com.jiuhou.auth.domain.vo.LoginVO;

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
     * @param user 用户信息
     * @return 注册结果
     */
    boolean register(User user);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    User getByUsername(String username);

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
}
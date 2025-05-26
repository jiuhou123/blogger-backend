package com.jiuhou.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiuhou.auth.domain.User;
import com.jiuhou.auth.domain.dto.LoginDTO;
import com.jiuhou.auth.domain.vo.LoginVO;
import com.jiuhou.auth.domain.vo.UserVO;
import com.jiuhou.auth.mapper.UserMapper;
import com.jiuhou.auth.service.UserService;
import com.jiuhou.auth.utils.JwtUtils;
import com.jiuhou.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户服务实现类
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        // 根据用户名查询用户
        User user = getByUsername(loginDTO.getUsername());
        if (user == null) {
            throw new ServiceException("用户不存在");
        }

        // 校验密码
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new ServiceException("密码错误");
        }

        // 校验账号状态
        if ("1".equals(user.getStatus())) {
            throw new ServiceException("账号已停用");
        }

        // 转换用户信息
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);

        // 生成token和refreshToken
        String token = jwtUtils.generateToken(user.getUserId());
        String refreshToken = jwtUtils.generateRefreshToken(user.getUserId());

        return new LoginVO(token, refreshToken, userVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean register(User user) {
        // 校验用户名是否存在
        if (getByUsername(user.getUsername()) != null) {
            throw new ServiceException("用户名已存在");
        }

        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 设置默认值
        user.setStatus("0");
        user.setDelFlag("0");

        return save(user);
    }

    @Override
    public User getByUsername(String username) {
        return getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
                .eq(User::getDelFlag, "0"));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePassword(Long userId, String oldPassword, String newPassword) {
        // 查询用户
        User user = getById(userId);
        if (user == null) {
            throw new ServiceException("用户不存在");
        }

        // 校验原密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new ServiceException("原密码错误");
        }

        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        return updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean resetPassword(Long userId) {
        // 查询用户
        User user = getById(userId);
        if (user == null) {
            throw new ServiceException("用户不存在");
        }

        // 重置密码为123456
        user.setPassword(passwordEncoder.encode("123456"));
        return updateById(user);
    }
}
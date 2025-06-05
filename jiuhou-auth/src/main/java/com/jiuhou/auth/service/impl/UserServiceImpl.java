package com.jiuhou.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiuhou.auth.domain.User;
import com.jiuhou.auth.domain.dto.LoginDTO;
import com.jiuhou.auth.domain.vo.LoginVO;
import com.jiuhou.auth.domain.vo.UserVO;
import com.jiuhou.auth.dto.UserRegisterRequest;
import com.jiuhou.auth.mapper.UserMapper;
import com.jiuhou.auth.service.UserService;
import com.jiuhou.auth.utils.JwtUtils;
import com.jiuhou.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jiuhou.auth.utils.ServiceExceptionMessageHolder;
import org.springframework.data.redis.core.RedisTemplate;
import java.util.concurrent.TimeUnit;

/**
 * 用户服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final RedisTemplate<String, String> redisTemplate;

    // Redis 刷新令牌存储 key 的前缀
    private static final String REFRESH_TOKEN_PREFIX = "auth:refresh:";
    // 刷新令牌在 Redis 中的过期时间（例如 7 天）
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60; // 秒

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        // 根据用户名查询用户
        User user = getByUsername(loginDTO.getUsername());
        if (user == null) {
            log.error("登录失败：用户 {} 不存在", loginDTO.getUsername());
            ServiceExceptionMessageHolder.setMessage("用户不存在");
            throw new ServiceException("用户不存在");
        }

        // 校验密码
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            log.error("登录失败：用户 {} 密码错误", loginDTO.getUsername());
            ServiceExceptionMessageHolder.setMessage("密码错误");
            throw new ServiceException("密码错误");
        }

        // 校验账号状态
        if ("1".equals(user.getStatus())) {
            log.error("登录失败：用户 {} 账号已停用", loginDTO.getUsername());
            ServiceExceptionMessageHolder.setMessage("账号已停用");
            throw new ServiceException("账号已停用");
        }

        // 转换用户信息
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);

        // 生成token和refreshToken
        String token = jwtUtils.generateToken(user.getId());
        String refreshToken = jwtUtils.generateRefreshToken(user.getId());

        // 将 refreshToken 存储到 Redis
        String redisKey = REFRESH_TOKEN_PREFIX + user.getId();
        redisTemplate.opsForValue().set(redisKey, refreshToken, REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);

        return new LoginVO(token, refreshToken, userVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean register(UserRegisterRequest registerRequest) {
        // 校验用户名是否存在
        if (getByUsername(registerRequest.getUsername()) != null) {
            ServiceExceptionMessageHolder.setMessage("用户名已存在");
            throw new ServiceException("用户名已存在");
        }

        // 校验邮箱是否存在
        if (getByEmail(registerRequest.getEmail()) != null) {
            ServiceExceptionMessageHolder.setMessage("邮箱已存在");
            throw new ServiceException("邮箱已存在");
        }

        // 校验密码复杂度
        if (registerRequest.getPassword() == null || registerRequest.getPassword().length() < 6) {
            ServiceExceptionMessageHolder.setMessage("密码长度不能少于6位");
            throw new ServiceException("密码长度不能少于6位");
        }
        // TODO: 添加更复杂的密码校验规则，例如：
        // - 是否包含大写字母
        // - 是否包含小写字母
        // - 是否包含数字
        // - 是否包含特殊字符

        // 创建用户对象
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setNickname(registerRequest.getNickname());
        user.setMobile(registerRequest.getMobile());
        user.setSex(registerRequest.getSex());
        user.setAvatar(registerRequest.getAvatar());
        // 加密密码
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
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
    public User getByEmail(String email) {
        return getOne(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, email)
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

    @Override
    public LoginVO refreshToken(String refreshToken) {
        // TODO: Implement refresh token validation and token generation logic
        // 1. 验证 refreshToken 的有效性（签名、过期时间、是否是我们系统颁发的）
        // 我们将通过 Redis 检查来代替 JWT 有效性验证，JWT 验证只作为辅助
        Long userId = jwtUtils.getUserIdFromToken(refreshToken);
        if (userId == null) {
            ServiceExceptionMessageHolder.setMessage("刷新令牌无效");
            throw new ServiceException("刷新令牌无效");
        }

        // 2. 从 Redis 中获取存储的 refreshToken
        String redisKey = REFRESH_TOKEN_PREFIX + userId;
        String storedRefreshToken = redisTemplate.opsForValue().get(redisKey);

        // 3. 检查 Redis 中存储的 refreshToken 是否存在且与提供的 refreshToken 匹配
        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            ServiceExceptionMessageHolder.setMessage("刷新令牌无效或已过期");
            throw new ServiceException("刷新令牌无效或已过期");
        }

        // 4. 根据用户ID查询用户
        User user = getById(userId);
        if (user == null || "1".equals(user.getStatus())) {
            ServiceExceptionMessageHolder.setMessage("用户不存在或已停用");
            throw new ServiceException("用户不存在或已停用");
        }

        // 5. 生成新的访问令牌和刷新令牌
        String newToken = jwtUtils.generateToken(userId);
        String newRefreshToken = jwtUtils.generateRefreshToken(userId);

        // 6. 更新 Redis 中的刷新令牌
        redisTemplate.opsForValue().set(redisKey, newRefreshToken, REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);

        // 7. 转换用户信息并返回新的令牌对
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return new LoginVO(newToken, newRefreshToken, userVO);
    }

    @Override
    public void logout(Long userId) {
        // 从 Redis 中删除刷新令牌
        String redisKey = REFRESH_TOKEN_PREFIX + userId;
        redisTemplate.delete(redisKey);
    }
}
package com.jiuhou.auth.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jiuhou.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class User extends BaseEntity {

    /** 用户ID */
    @TableId
    private Long userId;

    /** 用户账号 */
    private String username;

    /** 密码 */
    private String password;

    /** 用户昵称 */
    private String nickname;

    /** 用户邮箱 */
    private String email;

    /** 手机号码 */
    private String mobile;

    /** 用户性别（0男 1女 2未知） */
    private String sex;

    /** 头像地址 */
    private String avatar;

    /** 帐号状态（0正常 1停用） */
    private String status;

    /** 删除标志（0代表存在 1代表删除） */
    private String delFlag;
}
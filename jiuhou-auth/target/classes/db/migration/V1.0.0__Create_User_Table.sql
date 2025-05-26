CREATE TABLE sys_user (
    user_id         BIGSERIAL PRIMARY KEY,
    username        VARCHAR(30) NOT NULL UNIQUE,
    password        VARCHAR(100) NOT NULL,
    nickname        VARCHAR(30),
    email           VARCHAR(50),
    mobile          VARCHAR(11),
    sex             CHAR(1) DEFAULT '0',
    avatar          VARCHAR(100),
    status          CHAR(1) DEFAULT '0',
    del_flag        CHAR(1) DEFAULT '0',
    create_by       VARCHAR(64),
    create_time     TIMESTAMP,
    update_by       VARCHAR(64),
    update_time     TIMESTAMP,
    remark          VARCHAR(500)
);

COMMENT ON TABLE sys_user IS '用户信息表';
COMMENT ON COLUMN sys_user.user_id IS '用户ID';
COMMENT ON COLUMN sys_user.username IS '用户账号';
COMMENT ON COLUMN sys_user.password IS '密码';
COMMENT ON COLUMN sys_user.nickname IS '用户昵称';
COMMENT ON COLUMN sys_user.email IS '用户邮箱';
COMMENT ON COLUMN sys_user.mobile IS '手机号码';
COMMENT ON COLUMN sys_user.sex IS '用户性别（0男 1女 2未知）';
COMMENT ON COLUMN sys_user.avatar IS '头像地址';
COMMENT ON COLUMN sys_user.status IS '帐号状态（0正常 1停用）';
COMMENT ON COLUMN sys_user.del_flag IS '删除标志（0代表存在 1代表删除）';
COMMENT ON COLUMN sys_user.create_by IS '创建者';
COMMENT ON COLUMN sys_user.create_time IS '创建时间';
COMMENT ON COLUMN sys_user.update_by IS '更新者';
COMMENT ON COLUMN sys_user.update_time IS '更新时间';
COMMENT ON COLUMN sys_user.remark IS '备注'; 
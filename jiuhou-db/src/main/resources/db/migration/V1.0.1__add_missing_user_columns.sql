SET client_encoding TO 'UTF8';

-- 添加 sys_user 表缺失的字段
ALTER TABLE sys_user
ADD COLUMN nickname VARCHAR(50),
ADD COLUMN mobile VARCHAR(20),
ADD COLUMN sex CHAR(1),
ADD COLUMN avatar VARCHAR(255),
ADD COLUMN del_flag CHAR(1) DEFAULT '0';

COMMENT ON COLUMN sys_user.nickname IS '用户昵称';
COMMENT ON COLUMN sys_user.mobile IS '手机号码';
COMMENT ON COLUMN sys_user.sex IS '用户性别（0男 1女 2未知）';
COMMENT ON COLUMN sys_user.avatar IS '头像地址';
COMMENT ON COLUMN sys_user.del_flag IS '删除标志（0代表存在 1代表删除）'; 
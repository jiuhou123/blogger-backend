-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    status CHAR(1) DEFAULT '0',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE sys_user IS '系统用户表';
COMMENT ON COLUMN sys_user.id IS '用户ID';
COMMENT ON COLUMN sys_user.username IS '用户名';
COMMENT ON COLUMN sys_user.password IS '密码';
COMMENT ON COLUMN sys_user.email IS '邮箱';
COMMENT ON COLUMN sys_user.status IS '状态（0正常 1停用）';
COMMENT ON COLUMN sys_user.create_time IS '创建时间';
COMMENT ON COLUMN sys_user.update_time IS '更新时间';

-- 分类表
CREATE TABLE IF NOT EXISTS blog_category (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE blog_category IS '博客分类表';
COMMENT ON COLUMN blog_category.id IS '分类ID';
COMMENT ON COLUMN blog_category.name IS '分类名称';
COMMENT ON COLUMN blog_category.description IS '分类描述';
COMMENT ON COLUMN blog_category.create_time IS '创建时间';
COMMENT ON COLUMN blog_category.update_time IS '更新时间';

-- 标签表
CREATE TABLE IF NOT EXISTS blog_tag (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE blog_tag IS '博客标签表';
COMMENT ON COLUMN blog_tag.id IS '标签ID';
COMMENT ON COLUMN blog_tag.name IS '标签名称';
COMMENT ON COLUMN blog_tag.create_time IS '创建时间';
COMMENT ON COLUMN blog_tag.update_time IS '更新时间';

-- 文章表
CREATE TABLE IF NOT EXISTS blog_post (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    category_id BIGINT,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    status CHAR(1) DEFAULT '0',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES sys_user(id),
    FOREIGN KEY (category_id) REFERENCES blog_category(id)
);

COMMENT ON TABLE blog_post IS '博客文章表';
COMMENT ON COLUMN blog_post.id IS '文章ID';
COMMENT ON COLUMN blog_post.user_id IS '作者ID';
COMMENT ON COLUMN blog_post.category_id IS '分类ID';
COMMENT ON COLUMN blog_post.title IS '标题';
COMMENT ON COLUMN blog_post.content IS '内容';
COMMENT ON COLUMN blog_post.status IS '状态（0草稿 1发布）';
COMMENT ON COLUMN blog_post.create_time IS '创建时间';
COMMENT ON COLUMN blog_post.update_time IS '更新时间';

-- 文章标签关联表
CREATE TABLE IF NOT EXISTS blog_post_tag (
    post_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    PRIMARY KEY (post_id, tag_id),
    FOREIGN KEY (post_id) REFERENCES blog_post(id),
    FOREIGN KEY (tag_id) REFERENCES blog_tag(id)
);

COMMENT ON TABLE blog_post_tag IS '文章标签关联表';
COMMENT ON COLUMN blog_post_tag.post_id IS '文章ID';
COMMENT ON COLUMN blog_post_tag.tag_id IS '标签ID';

-- 评论表
CREATE TABLE IF NOT EXISTS blog_comment (
    id BIGSERIAL PRIMARY KEY,
    post_id BIGINT NOT NULL,
    user_id BIGINT,
    content TEXT NOT NULL,
    parent_id BIGINT,
    del_flag CHAR(1) DEFAULT '0',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES blog_post(id),
    FOREIGN KEY (user_id) REFERENCES sys_user(id)
);

COMMENT ON TABLE blog_comment IS '博客评论表';
COMMENT ON COLUMN blog_comment.id IS '评论ID';
COMMENT ON COLUMN blog_comment.post_id IS '文章ID';
COMMENT ON COLUMN blog_comment.user_id IS '用户ID';
COMMENT ON COLUMN blog_comment.content IS '评论内容';
COMMENT ON COLUMN blog_comment.parent_id IS '父评论ID';
COMMENT ON COLUMN blog_comment.del_flag IS '删除标志（0代表存在 1代表删除）';
COMMENT ON COLUMN blog_comment.create_time IS '创建时间';
COMMENT ON COLUMN blog_comment.update_time IS '更新时间';
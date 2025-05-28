-- �û���
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    status CHAR(1) DEFAULT '0',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE sys_user IS 'ϵͳ�û���';
COMMENT ON COLUMN sys_user.id IS '�û�ID';
COMMENT ON COLUMN sys_user.username IS '�û���';
COMMENT ON COLUMN sys_user.password IS '����';
COMMENT ON COLUMN sys_user.email IS '����';
COMMENT ON COLUMN sys_user.status IS '״̬��0���� 1ͣ�ã�';
COMMENT ON COLUMN sys_user.create_time IS '����ʱ��';
COMMENT ON COLUMN sys_user.update_time IS '����ʱ��';

-- �����
CREATE TABLE IF NOT EXISTS blog_category (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE blog_category IS '���ͷ����';
COMMENT ON COLUMN blog_category.id IS '����ID';
COMMENT ON COLUMN blog_category.name IS '��������';
COMMENT ON COLUMN blog_category.description IS '��������';
COMMENT ON COLUMN blog_category.create_time IS '����ʱ��';
COMMENT ON COLUMN blog_category.update_time IS '����ʱ��';

-- ��ǩ��
CREATE TABLE IF NOT EXISTS blog_tag (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE blog_tag IS '���ͱ�ǩ��';
COMMENT ON COLUMN blog_tag.id IS '��ǩID';
COMMENT ON COLUMN blog_tag.name IS '��ǩ����';
COMMENT ON COLUMN blog_tag.create_time IS '����ʱ��';
COMMENT ON COLUMN blog_tag.update_time IS '����ʱ��';

-- ���±�
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

COMMENT ON TABLE blog_post IS '�������±�';
COMMENT ON COLUMN blog_post.id IS '����ID';
COMMENT ON COLUMN blog_post.user_id IS '����ID';
COMMENT ON COLUMN blog_post.category_id IS '����ID';
COMMENT ON COLUMN blog_post.title IS '����';
COMMENT ON COLUMN blog_post.content IS '����';
COMMENT ON COLUMN blog_post.status IS '״̬��0�ݸ� 1������';
COMMENT ON COLUMN blog_post.create_time IS '����ʱ��';
COMMENT ON COLUMN blog_post.update_time IS '����ʱ��';

-- ���±�ǩ������
CREATE TABLE IF NOT EXISTS blog_post_tag (
    post_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    PRIMARY KEY (post_id, tag_id),
    FOREIGN KEY (post_id) REFERENCES blog_post(id),
    FOREIGN KEY (tag_id) REFERENCES blog_tag(id)
);

COMMENT ON TABLE blog_post_tag IS '���±�ǩ������';
COMMENT ON COLUMN blog_post_tag.post_id IS '����ID';
COMMENT ON COLUMN blog_post_tag.tag_id IS '��ǩID';

-- ���۱�
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

COMMENT ON TABLE blog_comment IS '�������۱�';
COMMENT ON COLUMN blog_comment.id IS '����ID';
COMMENT ON COLUMN blog_comment.post_id IS '����ID';
COMMENT ON COLUMN blog_comment.user_id IS '�û�ID';
COMMENT ON COLUMN blog_comment.content IS '��������';
COMMENT ON COLUMN blog_comment.parent_id IS '������ID';
COMMENT ON COLUMN blog_comment.del_flag IS 'ɾ����־��0������� 1����ɾ����';
COMMENT ON COLUMN blog_comment.create_time IS '����ʱ��';
COMMENT ON COLUMN blog_comment.update_time IS '����ʱ��';
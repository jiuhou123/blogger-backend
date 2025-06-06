SET client_encoding TO 'UTF8';

-- 添加 blog_post 表补充字段
ALTER TABLE blog_post ADD COLUMN views_count BIGINT DEFAULT 0;
COMMENT ON COLUMN blog_post.views_count IS '浏览量';

ALTER TABLE blog_post ADD COLUMN likes_count BIGINT DEFAULT 0;
COMMENT ON COLUMN blog_post.likes_count IS '点赞数';

ALTER TABLE blog_post ADD COLUMN comments_count BIGINT DEFAULT 0;
COMMENT ON COLUMN blog_post.comments_count IS '评论总数';

ALTER TABLE blog_post ADD COLUMN publish_time TIMESTAMP;
COMMENT ON COLUMN blog_post.publish_time IS '发布时间';

ALTER TABLE blog_post ADD COLUMN cover_image VARCHAR(255);
COMMENT ON COLUMN blog_post.cover_image IS '封面图片URL';

ALTER TABLE blog_post ADD COLUMN summary TEXT;
COMMENT ON COLUMN blog_post.summary IS '文章摘要';

ALTER TABLE blog_post ADD COLUMN del_flag CHAR(1) DEFAULT '0';
COMMENT ON COLUMN blog_post.del_flag IS '删除标志（0代表存在 1代表删除）';

-- 添加 blog_comment 表补充字段
ALTER TABLE blog_comment ADD COLUMN status CHAR(1) DEFAULT '0';
COMMENT ON COLUMN blog_comment.status IS '状态（0待审核 1已发布 2垃圾评论）'; 
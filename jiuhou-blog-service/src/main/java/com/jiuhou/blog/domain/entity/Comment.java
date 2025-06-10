package com.jiuhou.blog.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 博客评论实体类
 */
@Data
@TableName("blog_comment")
public class Comment {

    @TableId
    private Long id;

    private Long postId;

    private Long userId;

    private Long parentCommentId;

    private String content;

    private String status; // 例如: pending, approved, rejected

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
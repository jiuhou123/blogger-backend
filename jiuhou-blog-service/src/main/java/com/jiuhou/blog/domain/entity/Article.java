package com.jiuhou.blog.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 博客文章实体类
 */
@Data
@TableName("blog_post")
public class Article {

    @TableId
    private Long id;

    private Long userId;

    private Long categoryId;

    private String title;

    private String content;

    private String status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    // 新增字段
    private Long viewsCount;

    private Long likesCount;

    private Long commentsCount;

    private LocalDateTime publishTime;

    private String coverImage;

    private String summary;

    private String delFlag;
}
package com.jiuhou.blog.domain.vo;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 博客文章视图对象VO
 * 用于向前端展示文章的详细信息。
 */
@Data
public class BlogPostVO {

    /**
     * 文章ID
     */
    private Long id;
    /**
     * 文章标题
     */
    private String title;
    /**
     * 文章内容
     */
    private String content;
    /**
     * 分类ID
     */
    private Long categoryId;
    /**
     * 文章状态
     * 例如: draft (草稿), published (已发布)
     */
    private String status;
    /**
     * 封面图URL
     */
    private String coverImage;
    /**
     * 文章摘要
     */
    private String summary;
    /**
     * 浏览量
     */
    private Integer views;
    /**
     * 点赞数
     */
    private Integer likes;
    /**
     * 评论数
     */
    private Integer comments;
    /**
     * 发布时间
     */
    private LocalDateTime publishTime;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    // TODO: Add fields for category name, tags, etc.
}
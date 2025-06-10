package com.jiuhou.blog.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 文章更新请求DTO
 */
@Data
public class BlogPostUpdateRequest {

    /**
     * 文章ID
     * 必填，不能为null
     */
    @NotNull(message = "文章ID不能为空")
    private Long id;

    /**
     * 文章标题
     * 可选，如果提供则更新
     */
    private String title;
    /**
     * 文章内容
     * 可选，如果提供则更新
     */
    private String content;
    /**
     * 分类ID
     * 可选，如果提供则更新
     */
    private Long categoryId;
    /**
     * 标签ID列表
     * 可选，如果提供则更新
     */
    private List<Long> tagIds;
    /**
     * 文章状态
     * 可选，如果提供则更新。例如: draft (草稿), published (已发布)
     */
    private String status; // 例如: draft, published
    /**
     * 封面图URL
     * 可选，如果提供则更新
     */
    private String coverImage; // 封面图
    /**
     * 文章摘要
     * 可选，如果提供则更新
     */
    private String summary; // 摘要

    // TODO: Add other updatable fields as needed
}
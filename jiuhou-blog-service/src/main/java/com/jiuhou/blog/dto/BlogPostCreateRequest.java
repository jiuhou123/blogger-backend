package com.jiuhou.blog.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 文章创建请求DTO
 */
@Data
public class BlogPostCreateRequest {

    /**
     * 文章标题
     * 必填，不能是空白字符串
     */
    @NotBlank(message = "文章标题不能为空")
    private String title;

    /**
     * 文章内容
     * 必填，不能是空白字符串
     */
    @NotBlank(message = "文章内容不能为空")
    private String content;

    /**
     * 分类ID
     * 必填，不能为null
     */
    @NotNull(message = "分类ID不能为空")
    private Long categoryId;

    /**
     * 标签ID列表
     * 可选
     */
    private List<Long> tagIds;

    /**
     * 文章状态
     * 必填，不能是空白字符串。例如: draft (草稿), published (已发布)
     */
    @NotBlank(message = "文章状态不能为空")
    private String status; // 例如: draft, published

    /**
     * 封面图URL
     * 可选
     */
    private String coverImage; // 封面图
    /**
     * 文章摘要
     * 可选
     */
    private String summary; // 摘要
}
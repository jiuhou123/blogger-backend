package com.jiuhou.blog.dto;

import lombok.Data;

/**
 * 文章列表请求DTO
 * 用于封装查询文章列表时的各种过滤、分页和排序条件。
 */
@Data
public class BlogPostListRequest {

    /**
     * 页码
     * 默认为1
     */
    private Integer pageNum = 1; // 页码，默认为1
    /**
     * 每页数量
     * 默认为10
     */
    private Integer pageSize = 10; // 每页数量，默认为10

    // 过滤条件
    /**
     * 关键字
     * 可用于模糊匹配文章标题或内容
     */
    private String keyword;
    /**
     * 分类ID
     * 可用于按分类ID过滤文章
     */
    private Long categoryId; // 按分类ID过滤
    /**
     * 文章状态
     * 可用于按状态过滤文章，例如: draft (草稿), published (已发布)
     */
    private String status; // 按状态过滤 (e.g., draft, published)

    // 排序条件
    /**
     * 排序字段
     * 可选，例如: createTime (创建时间), publishTime (发布时间), views (浏览量)
     */
    private String sortBy; // 排序字段 (e.g., createTime, publishTime, views)
    /**
     * 排序顺序
     * 可选，例如: asc (升序), desc (降序)
     */
    private String sortOrder; // 排序顺序 (e.g., asc, desc)

    // TODO: Add other filter/sort criteria as needed
}
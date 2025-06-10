package com.jiuhou.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiuhou.blog.domain.entity.BlogPost;
import com.jiuhou.blog.domain.vo.BlogPostVO;
import com.jiuhou.blog.dto.BlogPostCreateRequest;
import com.jiuhou.blog.dto.BlogPostListRequest;
import com.jiuhou.blog.dto.BlogPostUpdateRequest;

/**
 * 博客文章服务接口
 */
public interface BlogPostService extends IService<BlogPost> {

    /**
     * 创建新博客文章
     *
     * @param request 创建文章的请求对象
     * @return 是否成功
     */
    boolean createPost(BlogPostCreateRequest request);

    /**
     * 根据ID获取文章详情
     *
     * @param id 文章ID
     * @return 文章详情VO，如果不存在则返回null
     */
    BlogPostVO getPostById(Long id);

    /**
     * 获取文章列表（分页、过滤、排序）
     *
     * @param request 获取文章列表的请求对象
     * @return 分页的文章列表VO
     */
    IPage<BlogPostVO> getPostList(BlogPostListRequest request);

    /**
     * 更新博客文章
     *
     * @param request 更新文章的请求对象
     * @return 是否成功
     */
    boolean updatePost(BlogPostUpdateRequest request);

    /**
     * 根据ID删除博客文章（软删除）
     *
     * @param id 文章ID
     * @return 是否成功
     */
    boolean deletePost(Long id);

    // TODO: 添加其他文章服务方法
}
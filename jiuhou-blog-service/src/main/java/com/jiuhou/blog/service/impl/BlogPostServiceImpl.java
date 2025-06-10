package com.jiuhou.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiuhou.blog.domain.entity.BlogPost;
import com.jiuhou.blog.domain.vo.BlogPostVO;
import com.jiuhou.blog.dto.BlogPostCreateRequest;
import com.jiuhou.blog.dto.BlogPostListRequest;
import com.jiuhou.blog.dto.BlogPostUpdateRequest;
import com.jiuhou.blog.mapper.BlogPostMapper;
import com.jiuhou.blog.service.BlogPostService;
import com.jiuhou.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 博客文章服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BlogPostServiceImpl extends ServiceImpl<BlogPostMapper, BlogPost> implements BlogPostService {

    // TODO: 注入其他需要的Mapper或Service (如 TagService, BlogPostTagMapper)

    @Override
    @Transactional
    public boolean createPost(BlogPostCreateRequest request) {
        BlogPost blogPost = new BlogPost();
        BeanUtils.copyProperties(request, blogPost);

        // 设置默认字段值
        blogPost.setViews(0);
        blogPost.setLikes(0);
        blogPost.setComments(0);
        blogPost.setDelFlag("0"); // 默认未删除
        blogPost.setCreateTime(LocalDateTime.now());
        // TODO: 获取当前用户ID并设置 createBy
        // blogPost.setCreateBy(getCurrentUserId());

        // 如果状态是 published 且 publishTime 为空，则设置当前时间
        if ("published".equals(blogPost.getStatus()) && blogPost.getPublishTime() == null) {
            blogPost.setPublishTime(LocalDateTime.now());
        }

        // 保存文章
        boolean saveSuccess = save(blogPost);

        if (saveSuccess && request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            // TODO: 处理标签关联，需要向 blog_post_tag 表中插入数据
            // 需要验证 tagIds 是否存在，并建立 blogPost.id 与 tagIds 列表中每个 tagId 的关联关系
            // for (Long tagId : request.getTagIds()) {
            // BlogPostTag postTag = new BlogPostTag();
            // postTag.setPostId(blogPost.getId());
            // postTag.setTagId(tagId);
            // blogPostTagMapper.insert(postTag);
            // }
        }

        return saveSuccess;
    }

    @Override
    public BlogPostVO getPostById(Long id) {
        // 根据ID查询文章，并排除已删除的
        BlogPost blogPost = getOne(new LambdaQueryWrapper<BlogPost>()
                .eq(BlogPost::getId, id)
                .eq(BlogPost::getDelFlag, "0"));

        if (blogPost == null) {
            return null; // 文章不存在或已删除
        }

        // 转换为 VO 对象
        BlogPostVO blogPostVO = new BlogPostVO();
        BeanUtils.copyProperties(blogPost, blogPostVO);

        // TODO: 获取分类名称和标签列表并设置到 VO 中

        return blogPostVO;
    }

    @Override
    public IPage<BlogPostVO> getPostList(BlogPostListRequest request) {
        // 构建分页对象
        Page<BlogPost> page = new Page<>(request.getPageNum(), request.getPageSize());

        // 构建查询条件
        LambdaQueryWrapper<BlogPost> queryWrapper = new LambdaQueryWrapper<>();

        // 过滤条件
        if (request.getCategoryId() != null) {
            queryWrapper.eq(BlogPost::getCategoryId, request.getCategoryId());
        }
        if (StringUtils.hasText(request.getStatus())) {
            queryWrapper.eq(BlogPost::getStatus, request.getStatus());
        }
        if (StringUtils.hasText(request.getKeyword())) {
            queryWrapper.like(BlogPost::getTitle, request.getKeyword());
        }
        queryWrapper.eq(BlogPost::getDelFlag, "0"); // 排除已删除的

        // 排序条件
        if (StringUtils.hasText(request.getSortBy())) {
            boolean isAsc = !StringUtils.hasText(request.getSortOrder())
                    || "asc".equalsIgnoreCase(request.getSortOrder());
            switch (request.getSortBy()) {
                case "createTime":
                    queryWrapper.orderBy(true, isAsc, BlogPost::getCreateTime);
                    break;
                case "publishTime":
                    queryWrapper.orderBy(true, isAsc, BlogPost::getPublishTime);
                    break;
                case "views":
                    queryWrapper.orderBy(true, isAsc, BlogPost::getViews);
                    break;
                case "likes":
                    queryWrapper.orderBy(true, isAsc, BlogPost::getLikes);
                    break;
                case "comments":
                    queryWrapper.orderBy(true, isAsc, BlogPost::getComments);
                    break;
                // TODO: Add other sortable fields
                default:
                    // Default sorting or throw exception
                    queryWrapper.orderByDesc(BlogPost::getCreateTime);
                    break;
            }
        } else {
            // 默认按创建时间倒序
            queryWrapper.orderByDesc(BlogPost::getCreateTime);
        }

        // 执行分页查询
        IPage<BlogPost> blogPostPage = page(page, queryWrapper);

        // 转换为 VO 对象列表
        List<BlogPostVO> blogPostVOList = blogPostPage.getRecords().stream()
                .map(blogPost -> {
                    BlogPostVO blogPostVO = new BlogPostVO();
                    BeanUtils.copyProperties(blogPost, blogPostVO);
                    // TODO: 获取分类名称和标签列表并设置到 VO 中
                    return blogPostVO;
                })
                .collect(Collectors.toList());

        // 构建分页结果 VO
        IPage<BlogPostVO> blogPostVOPage = new Page<>(blogPostPage.getCurrent(), blogPostPage.getSize(),
                blogPostPage.getTotal());
        blogPostVOPage.setRecords(blogPostVOList);

        return blogPostVOPage;
    }

    @Override
    @Transactional
    public boolean updatePost(BlogPostUpdateRequest request) {
        // 1. 检查文章是否存在且未删除
        BlogPost existingPost = getOne(new LambdaQueryWrapper<BlogPost>()
                .eq(BlogPost::getId, request.getId())
                .eq(BlogPost::getDelFlag, "0"));

        if (existingPost == null) {
            throw new ServiceException("文章不存在或已删除");
        }

        // 2. 拷贝可更新的属性
        // 使用 BeanUtils.copyProperties 可能覆盖不需要更新的字段，手动拷贝更安全
        if (StringUtils.hasText(request.getTitle())) {
            existingPost.setTitle(request.getTitle());
        }
        if (StringUtils.hasText(request.getContent())) {
            existingPost.setContent(request.getContent());
        }
        if (request.getCategoryId() != null) {
            existingPost.setCategoryId(request.getCategoryId());
        }
        if (StringUtils.hasText(request.getStatus())) {
            existingPost.setStatus(request.getStatus());
            // 如果状态更新为 published 且 publishTime 为空，则设置当前时间
            if ("published".equals(request.getStatus()) && existingPost.getPublishTime() == null) {
                existingPost.setPublishTime(LocalDateTime.now());
            }
        }
        if (StringUtils.hasText(request.getCoverImage())) {
            existingPost.setCoverImage(request.getCoverImage());
        }
        if (StringUtils.hasText(request.getSummary())) {
            existingPost.setSummary(request.getSummary());
        }

        // 3. 更新 updateTime 和 updateBy
        existingPost.setUpdateTime(LocalDateTime.now());
        // TODO: 获取当前用户ID并设置 updateBy
        // existingPost.setUpdateBy(getCurrentUserId());

        // 4. TODO: 处理标签关联更新 (先删除旧关联，再添加新关联)
        // if (request.getTagIds() != null) {
        // // 删除旧关联
        // blogPostTagMapper.delete(new LambdaQueryWrapper<BlogPostTag>()
        // .eq(BlogPostTag::getPostId, existingPost.getId()));
        // // 添加新关联
        // if (!request.getTagIds().isEmpty()) {
        // // 需要验证 tagIds 是否存在，并建立 existingPost.id 与 tagIds 列表中每个 tagId 的关联关系
        // for (Long tagId : request.getTagIds()) {
        // BlogPostTag postTag = new BlogPostTag();
        // postTag.setPostId(existingPost.getId());
        // postTag.setTagId(tagId);
        // blogPostTagMapper.insert(postTag);
        // }
        // }
        // }

        // 5. 执行更新操作
        return updateById(existingPost);
    }

    @Override
    @Transactional
    public boolean deletePost(Long id) {
        // 1. 检查文章是否存在且未删除
        BlogPost existingPost = getOne(new LambdaQueryWrapper<BlogPost>()
                .eq(BlogPost::getId, id)
                .eq(BlogPost::getDelFlag, "0"));

        if (existingPost == null) {
            // 文章不存在或已删除，认为操作成功，避免重复删除或对不存在的文章操作
            return true;
        }

        // 2. 执行软删除
        existingPost.setDelFlag("1"); // 标记为已删除
        existingPost.setUpdateTime(LocalDateTime.now());
        // TODO: 获取当前用户ID并设置 updateBy (执行删除操作的用户)
        // existingPost.setUpdateBy(getCurrentUserId());

        return updateById(existingPost);
    }

    // TODO: 实现其他文章服务方法
}
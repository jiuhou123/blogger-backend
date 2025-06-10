package com.jiuhou.blog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuhou.blog.domain.vo.BlogPostVO;
import com.jiuhou.blog.dto.BlogPostCreateRequest;
import com.jiuhou.blog.dto.BlogPostListRequest;
import com.jiuhou.blog.dto.BlogPostUpdateRequest;
import com.jiuhou.blog.service.BlogPostService;
import com.jiuhou.common.exception.ServiceException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/blog/posts")
@RequiredArgsConstructor
@Tag(name = "文章管理", description = "博客文章相关的API")
@Slf4j
public class BlogPostController {

    @Autowired
    private final BlogPostService blogPostService;

    @Operation(summary = "创建新文章")
    @PostMapping
    public ResponseEntity<Void> createBlogPost(@Valid @RequestBody BlogPostCreateRequest request) {
        log.info("Received create blog post request: {}", request);
        blogPostService.createPost(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "根据ID获取文章详情")
    @GetMapping("/{id}")
    public ResponseEntity<BlogPostVO> getBlogPostById(@Parameter(description = "文章ID") @PathVariable Long id) {
        BlogPostVO blogPostVO = blogPostService.getPostById(id);
        if (blogPostVO == null) {
            throw new ServiceException("文章不存在");
        }
        return ResponseEntity.ok(blogPostVO);
    }

    @Operation(summary = "获取文章列表")
    @GetMapping
    public ResponseEntity<IPage<BlogPostVO>> getBlogPostList(BlogPostListRequest request) {
        IPage<BlogPostVO> postListPage = blogPostService.getPostList(request);
        return ResponseEntity.ok(postListPage);
    }

    @Operation(summary = "更新文章")
    @PutMapping
    public ResponseEntity<Void> updateBlogPost(@Valid @RequestBody BlogPostUpdateRequest request) {
        boolean success = blogPostService.updatePost(request);
        if (!success) {
            throw new ServiceException("更新文章失败");
        }
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "根据ID删除文章（软删除）")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlogPost(@Parameter(description = "文章ID") @PathVariable Long id) {
        boolean success = blogPostService.deletePost(id);
        if (!success) {
            throw new ServiceException("删除文章失败");
        }
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "测试连通性")
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        log.info("Received ping request.");
        return ResponseEntity.ok("pong");
    }

    // TODO: 添加其他文章管理接口
}
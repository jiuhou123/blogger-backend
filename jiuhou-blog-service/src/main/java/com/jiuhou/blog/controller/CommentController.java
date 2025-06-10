package com.jiuhou.blog.controller;

import com.jiuhou.blog.domain.entity.Comment;
import com.jiuhou.blog.service.CommentService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

// TODO: Add Swagger annotations for API documentation
@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // TODO: Implement CRUD endpoints

    /**
     * 获取所有评论
     */
    @GetMapping
    public List<Comment> getAllComments() {
        return commentService.list();
    }

    /**
     * 根据ID获取评论
     */
    @GetMapping("/{id}")
    public Comment getCommentById(@PathVariable Long id) {
        return commentService.getById(id);
    }

    /**
     * 创建评论
     */
    @PostMapping
    public boolean createComment(@RequestBody Comment comment) {
        return commentService.save(comment);
    }

    /**
     * 更新评论
     */
    @PutMapping
    public boolean updateComment(@RequestBody Comment comment) {
        // 注意：传入的 comment 对象必须包含 ID
        return commentService.updateById(comment);
    }

    /**
     * 删除评论
     */
    @DeleteMapping("/{id}")
    public boolean deleteComment(@PathVariable Long id) {
        return commentService.removeById(id);
    }

}
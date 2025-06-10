package com.jiuhou.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiuhou.blog.domain.entity.Comment;
import com.jiuhou.blog.mapper.CommentMapper;
import com.jiuhou.blog.service.CommentService;
import org.springframework.stereotype.Service;

/**
 * 博客评论服务实现类
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    // TODO: Implement comment business logic methods defined in CommentService if
    // any

}
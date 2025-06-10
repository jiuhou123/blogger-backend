package com.jiuhou.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiuhou.blog.domain.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 博客评论 Mapper 接口
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

}
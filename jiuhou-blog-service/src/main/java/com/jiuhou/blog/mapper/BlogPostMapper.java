package com.jiuhou.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiuhou.blog.domain.entity.BlogPost;

import org.apache.ibatis.annotations.Mapper;

/**
 * 博客文章 Mapper 接口
 */
@Mapper
public interface BlogPostMapper extends BaseMapper<BlogPost> {

}
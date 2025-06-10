package com.jiuhou.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiuhou.blog.domain.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * 博客分类 Mapper 接口
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}
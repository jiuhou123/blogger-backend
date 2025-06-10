package com.jiuhou.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiuhou.blog.domain.entity.Category;
import com.jiuhou.blog.mapper.CategoryMapper;
import com.jiuhou.blog.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    // TODO: Implement category business logic methods
}
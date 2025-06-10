package com.jiuhou.blog.controller;

import com.jiuhou.blog.domain.entity.Category;
import com.jiuhou.blog.service.CategoryService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

// TODO: Add Swagger annotations for API documentation
@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // TODO: Implement CRUD endpoints

    /**
     * 获取所有分类
     */
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.list();
    }

    /**
     * 根据ID获取分类
     */
    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    /**
     * 创建分类
     */
    @PostMapping
    public boolean createCategory(@RequestBody Category category) {
        return categoryService.save(category);
    }

    /**
     * 更新分类
     */
    @PutMapping
    public boolean updateCategory(@RequestBody Category category) {
        // 注意：传入的 category 对象必须包含 ID
        return categoryService.updateById(category);
    }

    /**
     * 删除分类
     */
    @DeleteMapping("/{id}")
    public boolean deleteCategory(@PathVariable Long id) {
        return categoryService.removeById(id);
    }

    // TODO: Add endpoints for getting category tree

}
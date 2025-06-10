package com.jiuhou.blog.controller;

import com.jiuhou.blog.domain.entity.Tag;
import com.jiuhou.blog.service.TagService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

// TODO: Add Swagger annotations for API documentation
@RestController
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    // TODO: Implement CRUD endpoints

    /**
     * 获取所有标签
     */
    @GetMapping
    public List<Tag> getAllTags() {
        return tagService.list();
    }

    /**
     * 根据ID获取标签
     */
    @GetMapping("/{id}")
    public Tag getTagById(@PathVariable Long id) {
        return tagService.getById(id);
    }

    /**
     * 创建标签
     */
    @PostMapping
    public boolean createTag(@RequestBody Tag tag) {
        return tagService.save(tag);
    }

    /**
     * 更新标签
     */
    @PutMapping
    public boolean updateTag(@RequestBody Tag tag) {
        // 注意：传入的 tag 对象必须包含 ID
        return tagService.updateById(tag);
    }

    /**
     * 删除标签
     */
    @DeleteMapping("/{id}")
    public boolean deleteTag(@PathVariable Long id) {
        return tagService.removeById(id);
    }

}
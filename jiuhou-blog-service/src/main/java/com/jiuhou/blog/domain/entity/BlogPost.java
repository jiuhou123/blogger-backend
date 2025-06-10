package com.jiuhou.blog.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import java.time.LocalDateTime;

@Data
@ToString
@Accessors(chain = true)
@TableName("blog_post")
public class BlogPost {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String title;

    private String content;

    private Long categoryId;

    private String status; // 例如: draft, published

    private String coverImage; // 封面图

    private String summary; // 摘要

    private Integer views; // 浏览量

    private Integer likes; // 点赞数

    private Integer comments; // 评论数

    private LocalDateTime publishTime; // 发布时间

    @TableLogic
    private String delFlag; // 软删除标志 0-未删除 1-已删除

    private Long createBy;

    private LocalDateTime createTime;

    private Long updateBy;

    private LocalDateTime updateTime;
}
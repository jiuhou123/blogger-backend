package com.jiuhou.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiuhou.blog.domain.entity.Tag;
import org.apache.ibatis.annotations.Mapper;

/**
 * 标签 Mapper 接口
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

}
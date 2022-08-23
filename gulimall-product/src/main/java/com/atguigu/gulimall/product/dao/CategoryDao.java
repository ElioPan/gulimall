package com.atguigu.gulimall.product.dao;

import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author test
 * @email test@gmail.com
 * @date 2022-08-06 19:38:42
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
    public int updateCateById2(CategoryEntity category);
}

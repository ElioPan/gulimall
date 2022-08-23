package com.atguigu.gulimall.product.vo;

import com.atguigu.gulimall.product.entity.AttrGroupEntity;
import lombok.Data;

import java.util.List;

/**
 * @author
 * @create 2022-08-20-17:14
 */
@Data
public class GroupWithAttrVo extends AttrGroupEntity {
    private AttrVo[] attrs;
}

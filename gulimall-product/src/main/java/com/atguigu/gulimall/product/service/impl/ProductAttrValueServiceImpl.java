package com.atguigu.gulimall.product.service.impl;

import com.atguigu.gulimall.product.service.AttrService;
import com.atguigu.gulimall.product.vo.BaseAttrs;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;

import com.atguigu.gulimall.product.dao.ProductAttrValueDao;
import com.atguigu.gulimall.product.entity.ProductAttrValueEntity;
import com.atguigu.gulimall.product.service.ProductAttrValueService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service("productAttrValueService")
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueDao, ProductAttrValueEntity> implements ProductAttrValueService {
    @Resource
    AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductAttrValueEntity> page = this.page(
                new Query<ProductAttrValueEntity>().getPage(params),
                new QueryWrapper<ProductAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveAttrValue(Long spuId, List<BaseAttrs> baseAttrs) {
        if (baseAttrs == null || baseAttrs.size() == 0) {

        } else {
            List<ProductAttrValueEntity> productAttrValueEntities = new ArrayList<ProductAttrValueEntity>();

            for (BaseAttrs ba : baseAttrs) {
                ProductAttrValueEntity av = new ProductAttrValueEntity();
                av.setSpuId(spuId);
                av.setAttrId(ba.getAttrId());
                String name = attrService.getAttrNameByid(ba.getAttrId());
                av.setAttrName(name);
                av.setAttrValue(ba.getAttrValues());
                av.setQuickShow(ba.getShowDesc());
                productAttrValueEntities.add(av);
            }
            saveBatch(productAttrValueEntities);
        }
    }

}

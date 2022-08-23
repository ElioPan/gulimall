package com.atguigu.gulimall.product.service;

import com.atguigu.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gulimall.product.entity.ProductAttrValueEntity;
import com.atguigu.gulimall.product.vo.AttrGroupRelationVo;
import com.atguigu.gulimall.product.vo.AttrRespVo;
import com.atguigu.gulimall.product.vo.AttrVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.AttrEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author test
 * @email test@gmail.com
 * @date 2022-08-16 14:27:31
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrVo attr);

    PageUtils queryBasePage(Map<String, Object> params, long categoryId, String type);

    AttrRespVo getAttrInfo(Long attrId);

    void updateAttr(AttrRespVo attr);

    List<AttrEntity> getAttrListByGroupId(Long attrGroupId);

    void deleteRelation(AttrGroupRelationVo[] relation);

    PageUtils getNoRelationAttr(Long groupId, Map<String, Object> params);


    String getAttrNameByid(Long attrId);

    List<ProductAttrValueEntity> getListForSpu(Long spuId);

    void updateListForSpu(Long spuId, List<ProductAttrValueEntity> productAttrValueEntityList);
}


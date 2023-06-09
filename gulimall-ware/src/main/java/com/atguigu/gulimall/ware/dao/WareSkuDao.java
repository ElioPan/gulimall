package com.atguigu.gulimall.ware.dao;

import com.atguigu.gulimall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商品库存
 * 
 * @author test
 * @email test@gmail.com
 * @date 2022-08-07 00:30:49
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    int updateByPurchaseDone(@Param("skuId") Long skuId,@Param("wareId") Long wareId,@Param("skuNum") Integer skuNum);
}

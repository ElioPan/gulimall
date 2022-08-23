package com.atguigu.gulimall.ware.dao;

import com.atguigu.gulimall.ware.entity.PurchaseDetailEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * 
 * @author test
 * @email test@gmail.com
 * @date 2022-08-07 00:30:49
 */
@Mapper
public interface PurchaseDetailDao extends BaseMapper<PurchaseDetailEntity> {

    void updateStausByPurchaseIds(@Param("ids") List<Long> purchaseIds,@Param("status") int status);

    int updateStausByItemId(@Param("id") Long itemId,@Param("status") int status);
}

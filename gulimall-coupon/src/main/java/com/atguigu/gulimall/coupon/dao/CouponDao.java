package com.atguigu.gulimall.coupon.dao;

import com.atguigu.gulimall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author test
 * @email test@gmail.com
 * @date 2022-08-06 23:32:08
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}

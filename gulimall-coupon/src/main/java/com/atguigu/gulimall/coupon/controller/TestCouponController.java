package com.atguigu.gulimall.coupon.controller;

import com.atguigu.common.utils.R;
import com.atguigu.gulimall.coupon.entity.CouponEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author
 * @create 2022-08-07-10:51
 */
@RefreshScope
@RestController
public class TestCouponController {
    @GetMapping("/coupon/feignTest")
    public R test1(){
        CouponEntity couponEntity = new CouponEntity();
        couponEntity.setId(4L);
        couponEntity.setCouponName("巨大优惠");
        return R.ok().put("coupon",couponEntity);
    }

    @Value("${test.coupon}")
    String testValue;
    @GetMapping("/coupon/config")
    public String test2(){
        return testValue;
    }
}

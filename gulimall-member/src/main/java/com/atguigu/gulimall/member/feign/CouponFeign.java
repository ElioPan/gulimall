package com.atguigu.gulimall.member.feign;

import com.atguigu.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author
 * @create 2022-08-07-10:50
 */
@FeignClient(value = "gulimall-coupon")
public interface CouponFeign {
    @GetMapping("/coupon/feignTest")
    public R test1();
}

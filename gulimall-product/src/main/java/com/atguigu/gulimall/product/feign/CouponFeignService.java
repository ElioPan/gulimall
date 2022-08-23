package com.atguigu.gulimall.product.feign;


import com.atguigu.common.to.SkuReductionTo;
import com.atguigu.common.to.SpuBoundsTo;
import com.atguigu.common.utils.R;
import com.atguigu.gulimall.product.vo.SkuFullReductionEntity;
import com.atguigu.gulimall.product.vo.SkuLadderEntity;
import com.atguigu.gulimall.product.vo.SpuBoundsEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author
 * @create 2022-08-20-23:17
 */
@FeignClient("gulimall-coupon")
public interface CouponFeignService {
    @RequestMapping("/coupon/spubounds/save")
    public R save(@RequestBody SpuBoundsTo spuBoundsTo);

    @PostMapping("/coupon/skufullreduction/skuReductionSave")
    R skuReductionSave(@RequestBody SkuReductionTo skuReductionTo);
}

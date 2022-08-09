package com.atguigu.gulimall.member.controller;

import com.atguigu.common.utils.R;
import com.atguigu.gulimall.member.entity.MemberEntity;
import com.atguigu.gulimall.member.feign.CouponFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author
 * @create 2022-08-07-10:45
 */
@RestController
public class TestMemberController {
    @Autowired
    CouponFeign couponFeign;
    @GetMapping("/member/testFeign")
    public R test1(){
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setId(2L);
        memberEntity.setUsername("Elio");
        memberEntity.setGender(1);
        R coupon = couponFeign.test1();
        R member = R.ok().put("member", memberEntity).put("coupon",coupon);
        return member;
    }
}

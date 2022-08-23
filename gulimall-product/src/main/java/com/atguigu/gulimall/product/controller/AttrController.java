package com.atguigu.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.atguigu.common.utils.ValidatorUtils;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.atguigu.gulimall.product.entity.ProductAttrValueEntity;
import com.atguigu.gulimall.product.service.AttrGroupService;
import com.atguigu.gulimall.product.vo.AttrRespVo;
import com.atguigu.gulimall.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.atguigu.gulimall.product.entity.AttrEntity;
import com.atguigu.gulimall.product.service.AttrService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.R;

import javax.annotation.Resource;


/**
 * 商品属性
 *
 * @author test
 * @email test@gmail.com
 * @date 2022-08-16 14:27:31
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;


    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("product:attr:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
//    @RequiresPermissions("product:attr:info")
    public R info(@PathVariable("attrId") Long attrId) {
        AttrRespVo attr = attrService.getAttrInfo(attrId);

        return R.ok().put("attr", attr);
    }
    /**
     * 前后不一致，临时补充
     * */
    @RequestMapping("/{type}/list/{catelogId}")
//    @RequiresPermissions("product:attr:info")
    public R baselist(@RequestParam Map<String, Object> params,
                  @PathVariable("catelogId") long catelogId,
                      @PathVariable("type") String type){
//        PageUtils page = attrGroupService.queryPage(params);
        PageUtils page = attrService.queryBasePage(params,catelogId,type);

        return R.ok().put("page", page);
    }


    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("product:attr:save")
    public R save(@RequestBody AttrVo attr) {
        attrService.saveAttr(attr);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("product:attr:update")
    public R update(@RequestBody AttrRespVo attr) {
//        ValidatorUtils.validateEntity(attr);
        attrService.updateAttr(attr);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("product:attr:delete")
    public R delete(@RequestBody Long[] attrIds) {
        attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }
    /**
     * Get
     * /product/attr/base/listforspu/{spuId}
     * 查询商品规格
     * */
    @GetMapping("/base/listforspu/{spuId}")
    public R getListForSpu(@PathVariable("spuId") Long spuId){
        List<ProductAttrValueEntity> productAttrValueEntity = attrService.getListForSpu(spuId);
        return R.ok().put("data",productAttrValueEntity);
    }
    /**
     * Post
     * /product/attr/update/{spuId}
     * 修改商品规格
     * */
    @PostMapping("/update/{spuId}")
    public R updateListForSpu(@PathVariable("spuId") Long spuId,@RequestBody List<ProductAttrValueEntity> productAttrValueEntityList){
        attrService.updateListForSpu(spuId,productAttrValueEntityList);
        return R.ok();
    }


}

package com.atguigu.gulimall.ware.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.atguigu.common.utils.ValidatorUtils;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.atguigu.gulimall.ware.vo.MergeVo;
import com.atguigu.gulimall.ware.vo.PurchaseDoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.atguigu.gulimall.ware.entity.PurchaseEntity;
import com.atguigu.gulimall.ware.service.PurchaseService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.R;



/**
 * 采购信息
 *
 * @author test
 * @email test@gmail.com
 * @date 2022-08-07 00:30:49
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("ware:purchase:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("ware:purchase:info")
    public R info(@PathVariable("id") Long id){
        PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("ware:purchase:save")
    public R save(@RequestBody PurchaseEntity purchase){
        purchase.setCreateTime(new Date());
        purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("ware:purchase:update")
    public R update(@RequestBody PurchaseEntity purchase){
//        ValidatorUtils.validateEntity(purchase);
        purchaseService.updateById(purchase);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("ware:purchase:delete")
    public R delete(@RequestBody Long[] ids){
        purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }
    /**
     * 获取未分配采购单
     * Get
     * /ware/purchase/unreceive/list
     * */
    @GetMapping("/unreceive/list")
    public R getUnreceiveList(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.getUnreceiveList(params);
        return R.ok().put("page",page);
    }
    /**
     * 合并采购需求成单
     * Post
     * /ware/purchase/merge
     * */
    @PostMapping("/merge")
    public R merge(@RequestBody MergeVo mergeVo){
        purchaseService.merge(mergeVo);
        return R.ok();
    }
    /**
     * Post
     * /ware/purchase/received
     * 领取采购单
     * */
    @PostMapping("/received")
    public R receivePurchase(@RequestBody Long[] purchaseId){
        purchaseService.receivePurchase(purchaseId);
        return R.ok();
    }
    /**
     * Post
     * /ware/purchase/done
     * 完成采购单，更改库存
     * */
    @PostMapping("/done")
    public R purchaseDone(@RequestBody PurchaseDoneVo purchaseDoneVo){
        purchaseService.purchaseDone(purchaseDoneVo);
        return R.ok();
    }

}

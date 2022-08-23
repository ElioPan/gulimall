package com.atguigu.gulimall.ware.service;

import com.atguigu.gulimall.ware.vo.MergeVo;
import com.atguigu.gulimall.ware.vo.PurchaseDoneVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.gulimall.ware.entity.PurchaseEntity;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author test
 * @email test@gmail.com
 * @date 2022-08-07 00:30:49
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils getUnreceiveList(Map<String, Object> params);

    void merge(MergeVo mergeVo);

    void receivePurchase(Long[] purchaseId);

    void purchaseDone(PurchaseDoneVo purchaseDoneVo);
}


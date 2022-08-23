package com.atguigu.gulimall.ware.service.impl;

import com.atguigu.common.constant.WareConstant;
import com.atguigu.gulimall.ware.entity.PurchaseDetailEntity;
import com.atguigu.gulimall.ware.entity.WareSkuEntity;
import com.atguigu.gulimall.ware.service.PurchaseDetailService;
import com.atguigu.gulimall.ware.service.WareSkuService;
import com.atguigu.gulimall.ware.vo.MergeVo;
import com.atguigu.gulimall.ware.vo.PurchaseDoneItemVo;
import com.atguigu.gulimall.ware.vo.PurchaseDoneVo;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;

import com.atguigu.gulimall.ware.dao.PurchaseDao;
import com.atguigu.gulimall.ware.entity.PurchaseEntity;
import com.atguigu.gulimall.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Resource
    PurchaseDetailService purchaseDetailService;
    @Resource
    WareSkuService wareSkuService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils getUnreceiveList(Map<String, Object> params) {

        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>().in("status",
                        WareConstant.PurchaseStatusEnum.CREATED.getCode(),
                        WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode())
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void merge(MergeVo mergeVo) {
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        // 更新采购单
        Long purchaseId = mergeVo.getPurchaseId();
        if (purchaseId == null) {
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.ASSIGNED.getCode());
            save(purchaseEntity);
        } else {
            purchaseEntity.setId(purchaseId);
            purchaseEntity.setUpdateTime(new Date());
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.ASSIGNED.getCode());
            updateById(purchaseEntity);
        }


        // 更新采购需求单
        Long[] items = mergeVo.getItems();
        List<PurchaseDetailEntity> collect = Arrays.asList(items).stream().map(id -> {
            PurchaseDetailEntity pde = new PurchaseDetailEntity();
            pde.setId(id);
            pde.setPurchaseId(purchaseEntity.getId());
            pde.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());
            return pde;
        }).collect(Collectors.toList());
        purchaseDetailService.updateBatchById(collect);

    }

    @Transactional
    @Override
    public void receivePurchase(Long[] purchaseId) {
        List<PurchaseEntity> purchaseEntities = this.listByIds(Arrays.asList(purchaseId));
        List<PurchaseEntity> collect = purchaseEntities.stream().filter(p -> {
            if (p.getStatus() == WareConstant.PurchaseStatusEnum.CREATED.getCode() ||
                    p.getStatus() == WareConstant.PurchaseStatusEnum.ASSIGNED.getCode()) {
                return true;
            } else {
                return false;
            }
        }).map(p -> {
            p.setStatus(WareConstant.PurchaseStatusEnum.TOKEN.getCode());
            p.setUpdateTime(new Date());
            return p;
        }).collect(Collectors.toList());
        updateBatchById(collect);

        // 更新purchaseDetail
        List<Long> purchaseIds = collect.stream().map(item -> {
            return item.getId();
        }).collect(Collectors.toList());
        purchaseDetailService.updateStausByPurchaseIds(purchaseIds);
    }
    @Transactional
    @Override
    public void purchaseDone(PurchaseDoneVo purchaseDoneVo) {
        Long purchaseId = purchaseDoneVo.getId();
        List<PurchaseDoneItemVo> items = purchaseDoneVo.getItems();
        boolean flag = true;
        for(PurchaseDoneItemVo item : items){
            if(item.getStatus()== WareConstant.PurchaseDetailStatusEnum.FAILED.getCode()){
                flag = false;
            }else{
                PurchaseDetailEntity purchaseDetailEntity = purchaseDetailService.getById(item.getItemId());
                wareSkuService.updateByPurchaseDone(purchaseDetailEntity.getSkuId(),purchaseDetailEntity.getWareId(),purchaseDetailEntity.getSkuNum());
            }
            purchaseDetailService.updateByPurchaseDoneItemVo(item);
        }
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseId);
        purchaseEntity.setUpdateTime(new Date());
        if(flag == true){
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.FINISHED.getCode());
        }else {
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.EXCEPTION.getCode());
        }
        updateById(purchaseEntity);
    }

}

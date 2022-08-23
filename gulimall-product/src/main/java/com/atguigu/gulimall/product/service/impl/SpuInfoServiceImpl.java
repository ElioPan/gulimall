package com.atguigu.gulimall.product.service.impl;


import com.atguigu.common.to.SkuReductionTo;
import com.atguigu.common.to.SpuBoundsTo;
import com.atguigu.common.utils.R;
import com.atguigu.gulimall.product.entity.*;
import com.atguigu.gulimall.product.feign.CouponFeignService;
import com.atguigu.gulimall.product.service.*;
import com.atguigu.gulimall.product.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;

import com.atguigu.gulimall.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Slf4j
@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {
    @Resource
    SpuInfoDescService spuInfoDescService;
    @Resource
    SpuImagesService spuImagesService;
    @Resource
    ProductAttrValueService productAttrValueService;
    @Resource
    CouponFeignService couponFeignService;
    @Resource
    SkuInfoService skuInfoService;
    @Resource
    SkuSaleAttrValueService skuSaleAttrValueService;
    @Resource
    SkuImagesService skuImagesService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVo spuSaveVo) {
        // 保存spu info 基本信息
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuSaveVo, spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        save(spuInfoEntity);
        // 保存后才获得spu自增主键id
        Long spuId = spuInfoEntity.getId();
        // 保存spu description 描述信息 （数组）
        List<String> description = spuSaveVo.getDecript();
        /*
        StringBuilder descStr = new StringBuilder();
        for (int i = 0; i < description.size(); i++) {
            descStr.append(description.get(i));
            if(i < description.size()-1){
                descStr.append(",");
            }
        }
        */
        spuInfoDescService.saveDesc(spuId, description);
        // 保存spu image 图片信息（数组）
        List<String> images = spuSaveVo.getImages();
        spuImagesService.saveImg(spuId, images);
        //保存sms库中表sms_spu_bounds 积分信息
        SpuBoundsTo spuBoundsTo = new SpuBoundsTo();
        BeanUtils.copyProperties(spuSaveVo.getBounds(), spuBoundsTo);
        spuBoundsTo.setSpuId(spuId);
        R boundsR = couponFeignService.save(spuBoundsTo);
        if (boundsR.getCode() != 0) {
            log.error("远程调用coupon积分服务失败<<<<<<<<<<<<<<<");
        }
        // 保存表pms_product_attr_value表中属性 （数组）
        List<BaseAttrs> baseAttrs = spuSaveVo.getBaseAttrs();
        productAttrValueService.saveAttrValue(spuId, baseAttrs);
        // 保存sku （数组）
        // 此时以skus数组为基础！！！
        List<Skus> skus = spuSaveVo.getSkus();
        if (skus != null && skus.size() > 0) {
            for (Skus sku : skus) {
                // sku-2 保存sku info 基本属性
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(sku, skuInfoEntity);
                skuInfoEntity.setSpuId(spuId);
                skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
                skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
                List<Images> skuImages = sku.getImages();
                String defaultImage = "";
                for (Images i : skuImages) {
                    if (i.getDefaultImg() == 1) {
                        defaultImage = i.getImgUrl();
                    }
                }
                skuInfoEntity.setSkuDefaultImg(defaultImage);
                skuInfoService.save(skuInfoEntity);
                // 保存完毕才生成自增的sku主键id
                Long skuId = skuInfoEntity.getSkuId();
                // sku-1 保存表pms_sku_sale_attr_value属性（数组）
                // showDesc是attr表的属性
                List<SkuSaleAttrValueEntity> skuValueTable = new ArrayList<>();
                List<Attr> skuAttrs = sku.getAttr();
                for (int i = 0; i < skuAttrs.size(); i++) {
                    SkuSaleAttrValueEntity s = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(skuAttrs.get(i), s);
                    s.setSkuId(skuId);
                    skuValueTable.add(s);
                }
                skuSaleAttrValueService.saveBatch(skuValueTable);
                // sku-3 保存sku image 图片属性 （数组）

                List<SkuImagesEntity> skuImagesEntities = new ArrayList<>();
                for (int i = 0; i < skuImages.size(); i++) {
                    SkuImagesEntity s = new SkuImagesEntity();
                    s.setSkuId(skuId);
                    BeanUtils.copyProperties(skuImages.get(i), s);
                    if(StringUtils.isNotEmpty(s.getImgUrl())) {
                        skuImagesEntities.add(s);
                    }
                }
                skuImagesService.saveBatch(skuImagesEntities);
                // sku-4 descar 应该是用于sku info name拼串用,并不是，先略过
                // sku-5 保存sms库 sms_sku_ladder表折扣信息
                //       count status可能是判断条件，实际是>>>addOther是否参与其他优惠
                /*
                SkuLadderTo skuLadderTo = new SkuLadderTo();
                BeanUtils.copyProperties(sku, skuLadderTo);
                skuLadderTo.setSkuId(skuId);
                couponFeignService.save(skuLadderTo);

                 */
                // sku-6 保存sms库 sms_sku_full_reduction表折扣信息
                /*
                SkuFullReductionTo skuFullReductionTo = new SkuFullReductionTo();
                BeanUtils.copyProperties(sku, skuFullReductionTo);
                skuFullReductionTo.setSkuId(skuId);
                couponFeignService.save(skuFullReductionTo);

                 */
                // sku-7 member price （数组）可能是是为了获取priviledge_member_price字段值
                //       还是sms库member price表

                //重写远程调用coupon服务，优惠部分
                if(sku.getFullCount() > 0 || sku.getFullPrice().compareTo(new BigDecimal(0)) > 0) {
                    SkuReductionTo skuReductionTo = new SkuReductionTo();
                    BeanUtils.copyProperties(sku, skuReductionTo);
                    skuReductionTo.setSkuId(skuId);
                    couponFeignService.skuReductionSave(skuReductionTo);
                }
            }
        }
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();
        String catelogId = (String) params.get("catelogId");
        if(StringUtils.isNotEmpty(catelogId) && !"0".equals(catelogId)){
            wrapper.eq("catalog_id",catelogId);
        }
        String brandId = (String) params.get("brandId");
        if(StringUtils.isNotEmpty(brandId) && !"0".equals(brandId)){
            wrapper.eq("brand_id",brandId);
        }
        String status = (String) params.get("status");
        if(StringUtils.isNotEmpty(status)){
            wrapper.eq("publish_status",status);
        }
        String key = (String) params.get("key");
        if(StringUtils.isNotEmpty(key)){
            wrapper.and(w->{
               w.eq("id",key).or().like("spu_name",key);
            });
        }
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

}

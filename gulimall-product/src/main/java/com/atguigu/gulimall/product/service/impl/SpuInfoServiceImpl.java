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
        // ??????spu info ????????????
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuSaveVo, spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        save(spuInfoEntity);
        // ??????????????????spu????????????id
        Long spuId = spuInfoEntity.getId();
        // ??????spu description ???????????? ????????????
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
        // ??????spu image ????????????????????????
        List<String> images = spuSaveVo.getImages();
        spuImagesService.saveImg(spuId, images);
        //??????sms?????????sms_spu_bounds ????????????
        SpuBoundsTo spuBoundsTo = new SpuBoundsTo();
        BeanUtils.copyProperties(spuSaveVo.getBounds(), spuBoundsTo);
        spuBoundsTo.setSpuId(spuId);
        R boundsR = couponFeignService.save(spuBoundsTo);
        if (boundsR.getCode() != 0) {
            log.error("????????????coupon??????????????????<<<<<<<<<<<<<<<");
        }
        // ?????????pms_product_attr_value???????????? ????????????
        List<BaseAttrs> baseAttrs = spuSaveVo.getBaseAttrs();
        productAttrValueService.saveAttrValue(spuId, baseAttrs);
        // ??????sku ????????????
        // ?????????skus????????????????????????
        List<Skus> skus = spuSaveVo.getSkus();
        if (skus != null && skus.size() > 0) {
            for (Skus sku : skus) {
                // sku-2 ??????sku info ????????????
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
                // ??????????????????????????????sku??????id
                Long skuId = skuInfoEntity.getSkuId();
                // sku-1 ?????????pms_sku_sale_attr_value??????????????????
                // showDesc???attr????????????
                List<SkuSaleAttrValueEntity> skuValueTable = new ArrayList<>();
                List<Attr> skuAttrs = sku.getAttr();
                for (int i = 0; i < skuAttrs.size(); i++) {
                    SkuSaleAttrValueEntity s = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(skuAttrs.get(i), s);
                    s.setSkuId(skuId);
                    skuValueTable.add(s);
                }
                skuSaleAttrValueService.saveBatch(skuValueTable);
                // sku-3 ??????sku image ???????????? ????????????

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
                // sku-4 descar ???????????????sku info name?????????,?????????????????????
                // sku-5 ??????sms??? sms_sku_ladder???????????????
                //       count status?????????????????????????????????>>>addOther????????????????????????
                /*
                SkuLadderTo skuLadderTo = new SkuLadderTo();
                BeanUtils.copyProperties(sku, skuLadderTo);
                skuLadderTo.setSkuId(skuId);
                couponFeignService.save(skuLadderTo);

                 */
                // sku-6 ??????sms??? sms_sku_full_reduction???????????????
                /*
                SkuFullReductionTo skuFullReductionTo = new SkuFullReductionTo();
                BeanUtils.copyProperties(sku, skuFullReductionTo);
                skuFullReductionTo.setSkuId(skuId);
                couponFeignService.save(skuFullReductionTo);

                 */
                // sku-7 member price ????????????????????????????????????priviledge_member_price?????????
                //       ??????sms???member price???

                //??????????????????coupon?????????????????????
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

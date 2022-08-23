package com.atguigu.gulimall.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;

import com.atguigu.gulimall.product.dao.SpuImagesDao;
import com.atguigu.gulimall.product.entity.SpuImagesEntity;
import com.atguigu.gulimall.product.service.SpuImagesService;
import org.springframework.transaction.annotation.Transactional;


@Service("spuImagesService")
public class SpuImagesServiceImpl extends ServiceImpl<SpuImagesDao, SpuImagesEntity> implements SpuImagesService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuImagesEntity> page = this.page(
                new Query<SpuImagesEntity>().getPage(params),
                new QueryWrapper<SpuImagesEntity>()
        );

        return new PageUtils(page);
    }
    @Transactional
    @Override
    public void saveImg(Long spuId, List<String> images) {
        if (images == null || images.size() == 0) {

        } else {
            List<SpuImagesEntity> spuImagesEntities = new ArrayList<SpuImagesEntity>();
            for (int i = 0; i < images.size(); i++) {
                SpuImagesEntity image = new SpuImagesEntity();
                image.setSpuId(spuId);
                image.setImgUrl(images.get(i));
                spuImagesEntities.add(image);
            }
            saveBatch(spuImagesEntities);
        }
    }

}

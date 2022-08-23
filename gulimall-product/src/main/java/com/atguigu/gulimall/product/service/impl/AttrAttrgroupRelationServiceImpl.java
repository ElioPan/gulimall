package com.atguigu.gulimall.product.service.impl;

import com.atguigu.gulimall.product.dao.AttrDao;
import com.atguigu.gulimall.product.dao.AttrGroupDao;
import com.atguigu.gulimall.product.entity.AttrEntity;
import com.atguigu.gulimall.product.entity.AttrGroupEntity;
import com.atguigu.gulimall.product.vo.AttrGroupRelationVo;
import com.atguigu.gulimall.product.vo.AttrVo;
import com.atguigu.gulimall.product.vo.GroupWithAttrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;

import com.atguigu.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.atguigu.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gulimall.product.service.AttrAttrgroupRelationService;

import javax.annotation.Resource;


@Service("attrAttrgroupRelationService")
public class AttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity> implements AttrAttrgroupRelationService {
    @Resource
    AttrGroupDao attrGroupDao;
    @Resource
    AttrAttrgroupRelationDao attrGroupRelationDao;
    @Resource
    AttrDao attrDao;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrAttrgroupRelationEntity> page = this.page(
                new Query<AttrAttrgroupRelationEntity>().getPage(params),
                new QueryWrapper<AttrAttrgroupRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void addAttrRelation(AttrAttrgroupRelationEntity[] relation) {

        saveBatch(Arrays.asList(relation));

    }

    @Override
    public List<GroupWithAttrVo> getGroupWithAttrBycatId(Long catelogId) {
        List<AttrGroupEntity> groups = attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        List<GroupWithAttrVo> groupWithAttrVos = groups.stream().map((group) -> {
            GroupWithAttrVo groupWithAttrVo = new GroupWithAttrVo();
            BeanUtils.copyProperties(group, groupWithAttrVo);
            List<AttrAttrgroupRelationEntity> relationEntities = attrGroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>()
                    .eq("attr_group_id", group.getAttrGroupId()));
            List<Long> attrIds = relationEntities.stream().map((r) -> {
                return r.getAttrId();
            }).collect(Collectors.toList());
            List<AttrEntity> attrEntities = attrDao.selectBatchIds(attrIds);
            List<AttrVo> attrVos = attrEntities.stream().map((attr) -> {
                AttrVo attrVo = new AttrVo();
                BeanUtils.copyProperties(attr, attrVo);
                attrVo.setAttrGroupId(group.getAttrGroupId());
                return attrVo;
            }).collect(Collectors.toList());
            groupWithAttrVo.setAttrs(attrVos.toArray(new AttrVo[]{}));
            return groupWithAttrVo;
        }).collect(Collectors.toList());
        return groupWithAttrVos;
    }

}

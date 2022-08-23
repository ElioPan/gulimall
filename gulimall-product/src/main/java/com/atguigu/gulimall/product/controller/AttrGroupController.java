package com.atguigu.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.atguigu.common.utils.ValidatorUtils;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.atguigu.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gulimall.product.entity.AttrEntity;
import com.atguigu.gulimall.product.service.AttrAttrgroupRelationService;
import com.atguigu.gulimall.product.service.AttrService;
import com.atguigu.gulimall.product.service.CategoryService;
import com.atguigu.gulimall.product.vo.AttrGroupRelationVo;
import com.atguigu.gulimall.product.vo.AttrVo;
import com.atguigu.gulimall.product.vo.GroupWithAttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.atguigu.gulimall.product.entity.AttrGroupEntity;
import com.atguigu.gulimall.product.service.AttrGroupService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.R;

import javax.annotation.Resource;


/**
 * 属性分组
 *
 * @author test
 * @email test@gmail.com
 * @date 2022-08-06 19:38:42
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private AttrService attrService;
    @Resource
    private AttrAttrgroupRelationService relationService;
    /**
     * 列表
     */
    @RequestMapping("/list/{categoryId}")
//    @RequiresPermissions("product:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params,
                  @PathVariable("categoryId") long categoryId){
//        PageUtils page = attrGroupService.queryPage(params);
        PageUtils page = attrGroupService.queryPage(params,categoryId);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
//    @RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
        AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        Long catelogId = attrGroup.getCatelogId();
        Long[] catelogPath = categoryService.findCatelogPath(catelogId);
        attrGroup.setCatelogPath(catelogPath);

        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
        attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
        ValidatorUtils.validateEntity(attrGroup);
        attrGroupService.updateById(attrGroup);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds){
        attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }
    /**
     * 添加属性与分组关联
     * /product/attrgroup/{attrgroupId}/attr/relation
     * */
    @RequestMapping("/{attrgroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrgroupId") Long attrGroupId){
        List<AttrEntity> attrList =attrService.getAttrListByGroupId(attrGroupId);
        return R.ok().put("data",attrList);
    }
    /**
     * 删除分组内属性，即结束关系表内关系
     * */
    @PostMapping("/attr/relation/delete")
    public R relationDelete(@RequestBody AttrGroupRelationVo[] relation){
        attrService.deleteRelation(relation);
        return R.ok();
    }
    /**
     * 查询当前目录下，当前分组和其他分组没有关联的属性
     * /product/attrgroup/{attrgroupId}/noattr/relation
     * */
    @GetMapping("/{attrgroupId}/noattr/relation")
    public R noattrRelation(@PathVariable("attrgroupId")Long groupId,
                            @RequestParam Map<String,Object> params){
        PageUtils page = attrService.getNoRelationAttr(groupId,params);
        return R.ok().put("page",page);
    }
    /**
     * Post请求，属性分组添加分组操作
     * /product/attrgroup/attr/relation
     * */
    @PostMapping("/attr/relation")
    public R attrRelation(@RequestBody AttrAttrgroupRelationEntity[] relation){
        relationService.addAttrRelation(relation);
        return R.ok();
    }
    /**
     * 获取指定分类下所有分组和组内属性
     * Get
     * /product/attrgroup/{catelogId}/withattr
     * */
    @GetMapping("/{catelogId}/withattr")
    public R getAllGroupWithAttr(@PathVariable("catelogId") Long catelogId){
        List<GroupWithAttrVo> resultList = relationService.getGroupWithAttrBycatId(catelogId);
        return R.ok().put("data",resultList);
    }
}

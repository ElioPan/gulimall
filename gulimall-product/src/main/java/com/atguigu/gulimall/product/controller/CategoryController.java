package com.atguigu.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.atguigu.common.utils.ValidatorUtils;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.CategoryService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.R;



/**
 * 商品三级分类
 *
 * @author test
 * @email test@gmail.com
 * @date 2022-08-06 19:38:42
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 列表
     */
    @RequestMapping("/list/tree")
//    @RequiresPermissions("product:category:list")
    public R list(){
        List<CategoryEntity> categoryEntities = categoryService.listWithTree();
        List<CategoryEntity> collect = categoryEntities.stream().filter(category ->
                category.getParentCid() == 0).map((menu)->{
                    menu.setChildren(getChildCategory(menu,categoryEntities));
                    return menu;
        }).sorted((a,b) -> {
            return (a.getSort()==null? 0 : a.getSort()) - (b.getSort() == null? 0 : b.getSort());
        }).collect(Collectors.toList());
        return R.ok().put("data", collect);
    }
    private List<CategoryEntity> getChildCategory(CategoryEntity root,List<CategoryEntity> all){
        List<CategoryEntity> subCate = all.stream().filter((cate) -> {
            return cate.getParentCid() == root.getCatId();
        }).map((cate) -> {
            cate.setChildren(getChildCategory(cate,all));
            return cate;
        }).sorted((a,b) -> {
            return (a.getSort()==null? 0 : a.getSort()) - (b.getSort() == null? 0 : b.getSort());
        }).collect(Collectors.toList());
        return subCate;
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
//    @RequiresPermissions("product:category:info")
    public R info(@PathVariable("catId") Long catId){
        CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("data", category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("product:category:save")
    public R save(@RequestBody CategoryEntity category){
        categoryService.save(category);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("product:category:update")
    public R update(@RequestBody CategoryEntity category){
//        ValidatorUtils.validateEntity(category);
        categoryService.updateCascade(category);
        
        return R.ok();
    }
    @RequestMapping("/update/sort")
//    @RequiresPermissions("product:category:update")
    public R updateSort(@RequestBody CategoryEntity[] category){
//        ValidatorUtils.validateEntity(category);
        categoryService.updateBatchById(Arrays.asList(category));

        return R.ok();
    }
    @PostMapping("/update1")
    public R update1(@RequestBody CategoryEntity category){
        categoryService.updateById1(category);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("product:category:delete")
    public R delete(@RequestBody Long[] catIds){
        categoryService.removeMenusByIds(Arrays.asList(catIds));

        return R.ok();
    }

}

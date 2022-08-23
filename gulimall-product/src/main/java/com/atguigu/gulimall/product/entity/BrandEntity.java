package com.atguigu.gulimall.product.entity;

import com.atguigu.gulimall.product.valid.AddGroup;
import com.atguigu.gulimall.product.valid.CustomizedList;
import com.atguigu.gulimall.product.valid.UpdateGroup;
import com.atguigu.gulimall.product.valid.UpdateStatus;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.ibatis.annotations.Update;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 品牌
 *
 * @author test
 * @email test@gmail.com
 * @date 2022-08-06 19:38:42
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 品牌id
     */
    @TableId
    private Long brandId;
    /**
     * 品牌名
     */
    @NotBlank(groups = {AddGroup.class})
    private String name;
    /**
     * 品牌logo地址
     */
    @NotEmpty(groups = {AddGroup.class})
    @URL(message = "请输入正确的地址", groups = {AddGroup.class,UpdateGroup.class})
    private String logo;
    /**
     * 介绍
     */
    private String descript;
    /**
     * 显示状态[0-不显示；1-显示]
     */
    @NotNull(groups = {UpdateStatus.class})
    @CustomizedList(value = {0,1},groups = {AddGroup.class, UpdateStatus.class})
    private Integer showStatus;
    /**
     * 检索首字母
     */
    @NotEmpty(groups = {AddGroup.class})
    @Pattern(regexp = "^[a-zA-Z]$",message = "检索字母只能是一个字母"
                ,groups = {AddGroup.class, UpdateGroup.class})
    private String firstLetter;
    /**
     * 排序
     */
    @NotNull(groups = {AddGroup.class})
    @Min(value = 0,message = "排序字母要大于等于0"
            ,groups = {AddGroup.class,UpdateGroup.class})
    private Integer sort;

}

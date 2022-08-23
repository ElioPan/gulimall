package com.atguigu.gulimall.product.vo;

import lombok.Data;

/**
 * @author
 * @create 2022-08-17-13:34
 */
@Data
public class AttrRespVo extends AttrVo{
    private String groupName;
    private String catelogName;
    private Long[] catelogPath;
}

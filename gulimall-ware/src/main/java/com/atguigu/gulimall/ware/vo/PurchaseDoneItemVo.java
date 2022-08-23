package com.atguigu.gulimall.ware.vo;

import lombok.Data;

/**
 * @author
 * @create 2022-08-23-11:31
 */
@Data
public class PurchaseDoneItemVo {
    private Long itemId;
    private int status;
    private String reason;
}

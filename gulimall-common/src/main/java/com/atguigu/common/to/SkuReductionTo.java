package com.atguigu.common.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author
 * @create 2022-08-21-14:37
 */
@Data
public class SkuReductionTo {
    private Long skuId;
    private Integer fullCount;
    private BigDecimal discount;
    private BigDecimal price;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private List<MemberPrice> memberPrice;

}

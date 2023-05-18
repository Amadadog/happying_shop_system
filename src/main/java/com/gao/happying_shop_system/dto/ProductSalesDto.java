package com.gao.happying_shop_system.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.gao.happying_shop_system.entity.ProductSales;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: GaoWenQiang
 * @Date: 2023/03/21 21:31
 * @Description:
 */
@Data
public class ProductSalesDto extends ProductSales {
    //所选时段商品累计销量
    @TableField(exist = false)
    private Integer dishSales;
    //所选时段套餐累计销量
    @TableField(exist = false)
    private Integer setmealSales;
    //所选时段总销售额
    @TableField(exist = false)
    private BigDecimal totalSales;
}

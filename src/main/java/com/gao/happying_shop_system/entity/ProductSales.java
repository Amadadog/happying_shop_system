package com.gao.happying_shop_system.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: GaoWenQiang
 * @Date: 2023/03/19 16:34
 * @Description:
 */
@Data
public class ProductSales {
    private Long id;
    private Long dishId;
    private Long setmealId;
    private Integer dishNumber;
    private Integer setmealNumber;
    private BigDecimal allSalesAmount;
    private LocalDateTime CreateTime;
    //所选时段商品累计销量
    @TableField(exist = false)
    private Integer dishSales;
    //所选时段套餐累计销量
    @TableField(exist = false)
    private Integer setmealSales;
    //所选时段总销售额
    @TableField(exist = false)
    private BigDecimal totalSales;
    //所选时段目标id
    @TableField(exist = false)
    private Long targetId;
    //所选时段目标名称
    @TableField(exist = false)
    private String targetName;
    //所选时段目标数量
    @TableField(exist = false)
    private Integer targetNumber;
}
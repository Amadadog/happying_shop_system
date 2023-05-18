package com.gao.happying_shop_system.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.gao.happying_shop_system.entity.Dish;
import com.gao.happying_shop_system.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;
/**
* @Description:
* @Param:
* @return:
* @Author: GaoWenQiang
* @Date: 2022/11/3 10:11
*/
@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;

    @TableField(exist = false)
    private Integer saleNum;
}

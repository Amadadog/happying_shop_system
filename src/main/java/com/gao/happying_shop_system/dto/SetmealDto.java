package com.gao.happying_shop_system.dto;

import com.gao.happying_shop_system.entity.Setmeal;
import com.gao.happying_shop_system.entity.SetmealDish;
import lombok.Data;
import java.util.List;
/**
* @Description:
* @Param:
* @return:
* @Author: GaoWenQiang
* @Date: 2022/11/3 10:11
*/
@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}

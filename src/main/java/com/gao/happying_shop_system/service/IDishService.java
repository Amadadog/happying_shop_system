package com.gao.happying_shop_system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gao.happying_shop_system.utils.R;
import com.gao.happying_shop_system.dto.DishDto;
import com.gao.happying_shop_system.entity.Dish;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: GaoWenQiang
 * @Date: 2022/09/22 15:20
 * @Description:
 */
public interface IDishService extends IService<Dish>{
    /**
    * @description: 新增菜品，同时插入菜品对应的口味数据，需要操作两张表，dish、dish_flavor
    * @param: [dishDto]
    * @return: void
    * @author: GaoWenQiang
    * @date: 2022/11/4 11:17
    */
    public void saveWithFlavor(DishDto dishDto);
    /**
    * @description: 根据菜品id查询菜品信息和对应的口味
    * @param: [id]
    * @return: com.gao.happying_shop_system.dto.DishDto
    * @author: GaoWenQiang
    * @date: 2022/11/4 11:16
    */
    public DishDto getByIdWithDishAndFlavor(Long id);
    public R<Page> Page(int page, int pageSize, String name);

    /**
    * @description: 根据dto接收的数据保存商品信息和商品口味信息
    * @param: [dishDto]
    * @return: com.gao.happying_shop_system.utils.R<java.lang.String>
    * @author: GaoWenQiang
    * @date: 2022/11/8 14:40
    */
    public R<String> updateWithFlavor(DishDto dishDto);
    public R<List<DishDto>> list(Dish dish);
}

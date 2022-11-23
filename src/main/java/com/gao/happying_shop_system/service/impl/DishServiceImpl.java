package com.gao.happying_shop_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gao.happying_shop_system.utils.R;
import com.gao.happying_shop_system.dto.DishDto;
import com.gao.happying_shop_system.entity.Category;
import com.gao.happying_shop_system.entity.Dish;
import com.gao.happying_shop_system.entity.DishFlavor;
import com.gao.happying_shop_system.mapper.DishMapper;
import com.gao.happying_shop_system.service.ICategoryService;
import com.gao.happying_shop_system.service.IDishFlavorService;
import com.gao.happying_shop_system.service.IDishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: GaoWenQiang
 * @Date: 2022/09/22 15:22
 * @Description:
 */
@Slf4j
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements IDishService {
    @Autowired
    private IDishFlavorService dishFlavorService;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private ICategoryService categoryService;
    /**
     * @Description: 新增菜品，同时保存对应的口味数据
     * @Param: [dishDto]
     * @return: void
     * @Author: GaoWenQiang
     * @Date: 2022/11/3 10:15
     */
    @Transactional
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜品表dish
        this.save(dishDto);
        //菜品id
        Long dishId = dishDto.getId();
        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        //map是对每一项进行操作 不用reduce是因为reduce会操作完后进行累加
        //collect将当前流转换成一个集合
        flavors = flavors.stream()
                .map(flavor -> {
                    flavor.setDishId(dishId);
                    return flavor;
                }).collect(Collectors.toList());
        StringBuilder a = new StringBuilder("123");
        log.info(a.reverse().toString());
        //保存菜品口味数据到菜品口味表dish_flavor
        //批量保存
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public DishDto getByIdWithDishAndFlavor(Long id) {
        DishDto dishDto = new DishDto();
        Dish dish = dishMapper.selectById(id);

        BeanUtils.copyProperties(dish,dishDto);

        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,id);
        List<DishFlavor> dishFlavors = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
        dishDto.setFlavors(dishFlavors);

        return dishDto;
    }

    @Override
    public R<Page> Page(int page, int pageSize, String name) {
        Page<Dish> dishPage = new Page<>(page,pageSize);
        Page<DishDto>  dishDtoPage= new Page<>();

        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.like(StringUtils.isNotEmpty(name),Dish::getName,name);
        dishLambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);
        dishMapper.selectPage(dishPage,dishLambdaQueryWrapper);
        //对象拷贝
        BeanUtils.copyProperties(dishPage,dishDtoPage,"records");
        List<Dish> records = dishPage.getRecords();
        List<DishDto> dishDtos = records.stream().map(dish -> {
            //基本属性传递
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish, dishDto);
            //根据分类id查找分类对象再查找名称
            Long categoryId = dish.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if(category != null) {
                String categoryName = category.getName();
                //dto补充分类名称
                dishDto.setCategoryName(categoryName);
            }

            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(dishDtos);
        return R.success(dishDtoPage);
    }

    @Transactional
    @Override
    public R<String> updateWithFlavor(DishDto dishDto) {
        //更新dish表信息,向上转型
        log.info(dishDto.getId().toString());
        Boolean A = this.updateById(dishDto);
        //删除当前菜品的口味
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(dishFlavorLambdaQueryWrapper);
        //添加传入的口味数据
        List<DishFlavor> flavors = dishDto.getFlavors();
        Long dishId = dishDto.getId();
        flavors = flavors.stream()
                .map(flavor -> {
                    flavor.setDishId(dishId);
                    return flavor;
                }).collect(Collectors.toList());
        log.info(flavors.get(0).getDishId().toString());
        Boolean B = dishFlavorService.saveBatch(flavors);
        if (A && B) {
            return R.success("修改成功");
        } else {
            return R.error("修改失败");
        }
    }

    @Override
    public R<List<DishDto>> list(Dish dish) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //查分类id为x的商品
        dishLambdaQueryWrapper.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId());
        //查找状态为1的商品
        dishLambdaQueryWrapper.eq(Dish::getStatus,1);
        dishLambdaQueryWrapper.orderByDesc(Dish::getUpdateTime).orderByAsc(Dish::getSort);
        List<Dish> dishes = this.list(dishLambdaQueryWrapper);

        List<DishDto> dishDtos = dishes.stream().map(dishItem -> {
            //基本属性传递
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dishItem, dishDto);
            //根据分类id查找口味
            Long dishId = dishItem.getId();
            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
            List<DishFlavor> dishFlavors = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
            if(dishFlavors != null) {
                //dto补充菜品口味
                dishDto.setFlavors(dishFlavors);
            }

            return dishDto;
        }).collect(Collectors.toList());
        return R.success(dishDtos);
    }
}

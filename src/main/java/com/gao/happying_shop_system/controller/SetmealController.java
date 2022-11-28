package com.gao.happying_shop_system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gao.happying_shop_system.dto.DishDto;
import com.gao.happying_shop_system.entity.SetmealDish;
import com.gao.happying_shop_system.utils.R;
import com.gao.happying_shop_system.dto.SetmealDto;
import com.gao.happying_shop_system.entity.Setmeal;
import com.gao.happying_shop_system.service.ISetmealDishService;
import com.gao.happying_shop_system.service.ISetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: GaoWenQiang
 * @Date: 2022/11/08 15:12
 * @Description:套餐管理
 */
@Slf4j
@RestController
@RequestMapping("/happying_shop_system/setmeal")
public class SetmealController {
    @Autowired
    private ISetmealService setmealService;

    @Autowired
    private ISetmealDishService SetmealDishService;

    /**
     * @description: 新增套餐
     * @param: [setmealDto]
     * @return: com.gao.happying_shop_system.utils.R<java.lang.String>
     * @author: GaoWenQiang
     * @date: 2022/11/8 16:13
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        log.info(setmealDto.toString());
        setmealService.saveWithDish(setmealDto);
        return R.success("");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        return setmealService.Page(page, pageSize, name);
    }

    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable Integer status, @RequestParam ArrayList<Long> ids) {
        boolean re = true;
        log.info("ids:" + ids.get(0) + "status:" + status);
        LambdaQueryWrapper<Setmeal> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.in(ids != null, Setmeal::getId, ids);
        List<Setmeal> list = setmealService.list(dishLambdaQueryWrapper);
        list = list.stream().map(setmeal -> {
            setmeal.setStatus(status);
            return setmeal;
        }).collect(Collectors.toList());
        return setmealService.updateBatchById(list) ? R.success("修改成功") : R.error("修改失败");
    }
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto) {
        log.info(setmealDto.toString());

        return setmealService.updateWithDish(setmealDto);
    }
    @GetMapping("/{id}")
    public R<SetmealDto> getByIdWithSetmealAndDish(@PathVariable Long id){
        SetmealDto setmealDto = setmealService.getByIdWithSetmealAndDish(id);
        return R.success(setmealDto);
    }

    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        setmealService.delete(ids);
        return R.success("");
    }

    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus()!=null,Setmeal::getStatus,1);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }
    @GetMapping("/dish/{id}")
    public R<List<DishDto>> dish(@PathVariable("id") Long SetmealId){
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,SetmealId);
        //获取套餐里面的所有菜品，这个就是SetmealDish表里面的数据
        List<SetmealDish> setmealDishList = setmealDishService.list(queryWrapper);
        List<DishDto> dishDtoList = setmealDishList.stream().map((setmealDish -> {
            DishDto dishDto = new DishDto();
            //其实这个BeanUtils的拷贝是浅拷贝
            BeanUtils.copyProperties(setmealDish, dishDto);
            //这里是为了把套餐中的菜品的基本信息填充到dto中，比如菜品描述，菜品图片等菜品的基本信息
            Long dishId = setmealDish.getDishId();
            Dish dish = dishService.getById(dishId);
            BeanUtils.copyProperties(dish, dishDto);
            return dishDto;
        })).collect(Collectors.toList());

        return R.success(dishDtoList);
    }
}

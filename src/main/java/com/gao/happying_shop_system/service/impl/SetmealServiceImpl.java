package com.gao.happying_shop_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gao.happying_shop_system.utils.R;
import com.gao.happying_shop_system.utils.ServiceException;
import com.gao.happying_shop_system.dto.SetmealDto;
import com.gao.happying_shop_system.entity.Category;
import com.gao.happying_shop_system.entity.Setmeal;
import com.gao.happying_shop_system.entity.SetmealDish;
import com.gao.happying_shop_system.mapper.SetmealMapper;
import com.gao.happying_shop_system.service.ICategoryService;
import com.gao.happying_shop_system.service.ISetmealDishService;
import com.gao.happying_shop_system.service.ISetmealService;
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
 * @Date: 2022/09/22 15:25
 * @Description:
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements ISetmealService {

    @Autowired
    private ISetmealDishService setmealDishService;


    @Autowired
    private ICategoryService categoryService;

    @Override
    @Transactional()
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐基本信息，setmeal表
        this.save(setmealDto);
        //注：mp执行保存之后，将自动填充setmealId
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        //获取前端传来的商品数据，
        setmealDishes = setmealDishes.stream().map(setmealDish -> {
            setmealDish.setSetmealId(setmealDto.getId());
            return setmealDish;
        }).collect(Collectors.toList());

        //保存套餐和菜品的关联信息，setmeal_dish表
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    public R<Page> Page(int page, int pageSize, String name) {
        Page<Setmeal> setmealPage = new Page<>(page,pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.like(name != null,Setmeal::getName,name);
        setmealLambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        this.page(setmealPage,setmealLambdaQueryWrapper);

        BeanUtils.copyProperties(setmealPage,setmealDtoPage,"records");
        List<Setmeal> records = setmealPage.getRecords();
        List<SetmealDto> sRecords = records.stream().map(setmeal -> {
            Category category = categoryService.getById(setmeal.getCategoryId());
            SetmealDto setmealDto = new SetmealDto();
            if(category != null) {
                //因listpage泛型不同，不可之间拷贝，故对集合中的每个元素进行拷贝，并赋上categoryname

                BeanUtils.copyProperties(setmeal,setmealDto);

                String categoryName =  category.getName();

                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        setmealDtoPage.setRecords(sRecords);
        return R.success(setmealDtoPage);
    }

    @Override
    @Transactional
    public R<String> updateWithDish(SetmealDto setmealDto) {
        boolean A = this.updateById(setmealDto);
        //清空数据库中的套餐中的商品
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());
        setmealDishService.remove(setmealDishLambdaQueryWrapper);
        //保存套餐中的商品
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map(setmealDish -> {
            setmealDish.setSetmealId(setmealDto.getId());
            return setmealDish;
        }).collect(Collectors.toList());

        boolean B = setmealDishService.saveBatch(setmealDishes);

        return A && B? R.success("修改成功"): R.success("修改失败");
    }

    @Override
    public SetmealDto getByIdWithSetmealAndDish(Long id) {
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);

        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> setmealDishes = setmealDishService.list(setmealDishLambdaQueryWrapper);

        setmealDto.setSetmealDishes(setmealDishes);
        return setmealDto;
    }


    @Override
    @Transactional
    public void delete(List<Long> ids) {
        //1.统计提交的商品中售卖的数量，大于1则不能删除
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.in(ids != null,Setmeal::getId,ids).eq(Setmeal::getStatus,1);
        int count = this.count(setmealLambdaQueryWrapper);
        if (count > 0) {
            throw new ServiceException("套餐正在售卖中，无法删除");
        }
        //删除套餐
        this.removeByIds(ids);

        //删除套餐商品关系表
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(setmealDishLambdaQueryWrapper);
    }
}

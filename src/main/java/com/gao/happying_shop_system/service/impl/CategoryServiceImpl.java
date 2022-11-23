package com.gao.happying_shop_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gao.happying_shop_system.utils.R;
import com.gao.happying_shop_system.utils.ServiceException;
import com.gao.happying_shop_system.entity.Category;
import com.gao.happying_shop_system.entity.Dish;
import com.gao.happying_shop_system.entity.Setmeal;
import com.gao.happying_shop_system.mapper.CategoryMapper;
import com.gao.happying_shop_system.service.ICategoryService;
import com.gao.happying_shop_system.service.IDishService;
import com.gao.happying_shop_system.service.ISetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: GaoWenQiang
 * @Date: 2022/09/14 21:20
 * @Description:根据id删除分类，删除之前需要进行判断
 */
@Service
public class CategoryServiceImpl  extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private IDishService dishService;
    @Autowired
    private ISetmealService setmealService;
    @Override
    public R<String> saveCategory(Category category) {
        categoryMapper.insert(category);
        return R.success("新增分类成功");
    }

    @Override
    public R<Page> page(int page, int pageSize) {
        Page<Category> pageInfo = new Page<>(page,pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        categoryMapper.selectPage(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    @Override
    public void delete(Long id) {
        //查询构造器
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count = dishService.count(dishLambdaQueryWrapper);

        //查询当前分类是否关联了菜品，如果已经关联，抛出一个业务异常
        if(count > 0){
            //已经关联菜品，抛出一个异常
            throw new ServiceException("当前分类下关联了菜品，不能删除");
        }
        //查询当前分类是否关联了套餐，如果已经关联，抛出一个业务异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if(count2 > 0){
            //已经关联套餐，抛出一个异常
            throw new ServiceException("当前分类下关联了套餐，不能删除");
        }
        //正常删除分类
        super.removeById(id);
    }


}

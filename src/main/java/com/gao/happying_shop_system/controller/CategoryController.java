package com.gao.happying_shop_system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gao.happying_shop_system.utils.R;
import com.gao.happying_shop_system.entity.Category;
import com.gao.happying_shop_system.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: GaoWenQiang
 * @Date: 2022/09/14 21:27
 * @Description:
 */
@RestController
@RequestMapping("/happying_shop_system/category")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @PostMapping
    public R<String> saveCategory(@RequestBody Category category){
        return categoryService.saveCategory(category);
    }
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize) {
        return categoryService.page(page,pageSize);
    }
    @DeleteMapping
    public R<String> delete(Long id) {
        categoryService.delete(id);
        return R.success("分类删除成功");
    }
    /***
    * @Description: 根据id修改分类信息
    * @Param: [category]
    * @return: com.gao.happying_shop_system.utils.R<java.lang.String>
    * @Author: GaoWenQiang
    * @Date: 2022/11/2 15:35
    */
    @PutMapping
    public R<String> update(@RequestBody Category category) {
        categoryService.updateById(category);
        return R.success("修改成功");
    }
    /***
    * @Description: 根据条件查询分类数据
    * @Param: [category]
    * @return: com.gao.happying_shop_system.utils.R<java.util.List<com.gao.happying_shop_system.entity.Category>>
    * @Author: GaoWenQiang
    * @Date: 2022/11/2 15:35
    */
    @GetMapping("/list")
    public R<List<Category>> list(Category category) {
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(category.getType()!= null,Category::getType,category.getType());
        //先按照排序序号排序，如果相同序号，则按照更新时间排序
        lambdaQueryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(lambdaQueryWrapper);
        return  R.success(list);
    }
}

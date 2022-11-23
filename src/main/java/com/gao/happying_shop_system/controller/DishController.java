package com.gao.happying_shop_system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gao.happying_shop_system.utils.R;
import com.gao.happying_shop_system.dto.DishDto;
import com.gao.happying_shop_system.entity.Dish;
import com.gao.happying_shop_system.service.IDishFlavorService;
import com.gao.happying_shop_system.service.IDishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: GaoWenQiang
 * @Date: 2022/11/02 15:15
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/happying_shop_system/dish")
public class DishController {
    @Autowired
    private IDishService dishService;
    
    @Autowired
    private IDishFlavorService dishFlavorService;
    /**
    * @Description: 新增菜品
    * @Param: [dishDto]
    * @return: com.gao.happying_shop_system.utils.R<java.lang.String>
    * @Author: GaoWenQiang
    * @Date: 2022/11/3 16:55
    */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());

        dishService.saveWithFlavor(dishDto);

        return R.success("新增菜品成功");
    }
    /**
    * @Description: 菜品信息分页查询
    * @Param: [page, pageSize, name]
    * @return: com.gao.happying_shop_system.utils.R<com.baomidou.mybatisplus.extension.plugins.pagination.Page>
    * @Author: GaoWenQiang
    * @Date: 2022/11/3 16:54
    */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){

        return dishService.Page(page,pageSize,name);
    }
    
    /**
    * @Description: 查询需要回显的信息。菜品信息和对应的口味信息
    * @Param: [id]
    * @return: com.gao.happying_shop_system.utils.R<com.gao.happying_shop_system.dto.DishDto>
    * @Author: GaoWenQiang
    * @Date: 2022/11/4 10:56
    */
    @GetMapping("/{id}")
    public R<DishDto> getByIdWithDishAndFlavor(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithDishAndFlavor(id);
        return R.success(dishDto);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());

        return dishService.updateWithFlavor(dishDto);
    }
    @DeleteMapping
    public R<String> deleteById(@RequestParam String ids[]) {
        log.info("ids:" + ids.toString());
        return dishService.removeByIds(Arrays.asList(ids))?R.success("删除成功"):R.error("删除失败");
    }
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable Integer status, @RequestParam ArrayList<Long> ids){
        log.info("ids:" + ids.get(0)+ "status:" + status);
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.in(ids != null,Dish::getId,ids);
        List<Dish> list = dishService.list(dishLambdaQueryWrapper);
//        for (Dish dish : list) {
//            dish.setStatus(status);
//            re = re && dishService.updateById(dish);
//        }
        list = list.stream().map(dish -> {
            dish.setStatus(status);
            return dish;
        }).collect(Collectors.toList());
        return dishService.updateBatchById(list)?R.success("修改成功"):R.error("修改失败");
    }
    /**
    * @description: 跟套餐id查询商品数据
    * @param: [dish]
    * @return: com.gao.happying_shop_system.utils.R<java.util.List<com.gao.happying_shop_system.entity.Dish>>
    * @author: GaoWenQiang
    * @date: 2022/11/8 15:36
    */
    /*@GetMapping("/list")
    public R<List<Dish>> list(Dish dish){
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //查分类id为x的商品
        dishLambdaQueryWrapper.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId());
        //查找状态为1的商品
        dishLambdaQueryWrapper.eq(Dish::getStatus,1);
        dishLambdaQueryWrapper.orderByDesc(Dish::getUpdateTime).orderByAsc(Dish::getSort);
        return dishService.list(dishLambdaQueryWrapper) != null?R.success(dishService.list(dishLambdaQueryWrapper)):R.error("");
    }*/
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        return dishService.list(dish);
    }
}

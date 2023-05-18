package com.gao.happying_shop_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gao.happying_shop_system.entity.*;
import com.gao.happying_shop_system.mapper.ProductSalesMapper;
import com.gao.happying_shop_system.service.*;
import com.gao.happying_shop_system.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: GaoWenQiang
 * @Date: 2023/03/19 16:54
 * @Description:
 */
@Slf4j
@Service
public class ProductSalesServiceImpl extends ServiceImpl<ProductSalesMapper, ProductSales> implements IProductSalesService {
    @Autowired
    private IDishService dishService;
    @Autowired
    private ISetmealService setmealService;
    @Autowired
    private IUserService userService;
    @Override
    public R<List<ProductSales>> getSetmealSales(String beginTime, String endTime) {
        QueryWrapper<ProductSales> wrapper = new QueryWrapper<>();
        wrapper.between("create_time", beginTime, endTime)
                .groupBy("dish_id", "setmeal_id")
                .select("dish_id", "setmeal_id",
                        "SUM(setmeal_number) as setmeal_sales",
                        "SUM(all_sales_amount) as total_sales")
                .orderByDesc("setmeal_sales");
        List<ProductSales> productSalesList = this.list(wrapper);

        return R.success(productSalesList);
    }
    @Override
    public R<List<ProductSales>> getDishSales(String beginTime, String endTime) {
        QueryWrapper<ProductSales> wrapper = new QueryWrapper<>();
        wrapper.between("create_time", beginTime, endTime)
                .groupBy("dish_id", "setmeal_id")
                .select("dish_id", "setmeal_id",
                        "SUM(dish_number) as dish_sales",
                        "SUM(all_sales_amount) as total_sales")
                .orderByDesc("dish_sales");
        List<ProductSales> productSalesList = this.list(wrapper);

        return R.success(productSalesList);
    }
    /**
    * @description:  获取每天的销售额
    * @param: [beginTime, endTime]
    * @return: com.gao.happying_shop_system.utils.R<java.util.List<java.util.Map<java.lang.String,java.lang.Object>>>
    * @author: GaoWenQiang
    * @date: 2023/3/28 22:18
    */
    @Override
    public R<List<Map<String, Object>>> getMoney(String beginTime, String endTime) {

        //按每天分组查询

        QueryWrapper<ProductSales> wrapper = new QueryWrapper<>();
        wrapper.between("create_time", beginTime, endTime)
                .groupBy("DATE_FORMAT(create_time, '%Y-%m-%d')")
                .select("DATE_FORMAT(create_time, '%Y-%m-%d') as date",
                        "SUM(all_sales_amount) as total_sales")
                .orderByAsc("date");
        return R.success(this.listMaps(wrapper));
    }

    @Override
    public Integer getSalesNumber(String id) {
        LambdaQueryWrapper<ProductSales> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductSales::getDishId, id)
                .or()
                .eq(ProductSales::getSetmealId, id);
        List<ProductSales> productSales = this.list(wrapper);
        //累加productSales中的dishNumber或setmealNumber
        Integer dishNumber = productSales.stream().map(ProductSales::getDishNumber).reduce(Integer::sum).orElse(0);
        Integer setmealNumber = productSales.stream().map(ProductSales::getSetmealNumber).reduce(Integer::sum).orElse(0);
        if (dishNumber == 0) {
            return setmealNumber;
        } else if (setmealNumber == 0) {
            return dishNumber;
        } else {
            return 0;
        }
    }
    /***
    * @description: 获取指定时间范围内的用户数量
    * @param: [beginTime, endTime]
    * @return: com.gao.happying_shop_system.utils.R<java.util.ArrayList<java.lang.Integer>>
    * @author: GaoWenQiang
    * @date: 2023/4/6 21:31
    */
    @Override
    public R<ArrayList<Integer>> getUserNumbers(String beginTime, String endTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date startDate = dateFormat.parse(beginTime);
            Date endDate = dateFormat.parse(endTime);

            ArrayList<Integer> userNumbers = new ArrayList<>();

            while (startDate.compareTo(endDate) <= 0) {
                LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
                userLambdaQueryWrapper.le(User::getCreateTime, startDate);
                Integer count = userService.count(userLambdaQueryWrapper);
                userNumbers.add(count);
                Calendar c = Calendar.getInstance();
                c.setTime(startDate);
                c.add(Calendar.DATE, 1);
                startDate = c.getTime();
            }

            return R.success(userNumbers);
        } catch (ParseException e) {
            e.printStackTrace();
            return R.error("日期格式不正确");
        }
    }

    /***
    * @description: 获取指定时间范围内的销售额前十的商品和销售额
    * @param: [beginTime, endTime]
    * @return: com.gao.happying_shop_system.utils.R<java.util.List<com.gao.happying_shop_system.entity.ProductSales>>
    * @author: GaoWenQiang
    * @date: 2023/4/6 21:31
    */
    @Override
    public R<List<ProductSales>> getTop10Sales(String beginTime, String endTime) {
        QueryWrapper<ProductSales> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("create_time", beginTime, endTime);
        queryWrapper.select("COALESCE(dish_id, setmeal_id) AS id", "SUM(COALESCE(dish_number, 0) + COALESCE(setmeal_number, 0)) AS sales");
        queryWrapper.groupBy("COALESCE(dish_id, setmeal_id)");
        queryWrapper.orderByDesc("sales");
        queryWrapper.last("LIMIT 10");
        List<Map<String, Object>> maps = this.listMaps(queryWrapper);
        List<ProductSales> productSales = maps.stream().map(map -> {
            ProductSales productSales1 = new ProductSales();
            Long itemId = (Long) map.get("id");
            productSales1.setTargetId(itemId);
            Dish dish = dishService.getById(itemId);
            Setmeal setmeal = setmealService.getById(itemId);
            if (dish != null) {
                productSales1.setTargetName(dish.getName());
            } else if (setmeal != null) {
                productSales1.setTargetName(setmeal.getName());
            }
            //log.info("salesType:{}", map.get("sales").getClass());
            //map.get("sales"))的返回类型是BigDecimal
            productSales1.setTargetNumber(((BigDecimal) map.get("sales")).intValue());
            return productSales1;
        }).collect(Collectors.toList());
        return R.success(productSales);
    }

    @Override
    public R<List<Map<String, Object>>> getRegisterNumbers(String beginTime, String endTime) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.between("create_time", beginTime, endTime)
                .select("DATE_FORMAT(create_time, '%Y-%m-%d') as date", "COUNT(*) as count")
                .groupBy("DATE_FORMAT(create_time, '%Y-%m-%d')")
                .orderByDesc("date");

        List<Map<String, Object>> result = userService.listMaps(wrapper);
        return R.success(result);
    }

}

package com.gao.happying_shop_system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gao.happying_shop_system.entity.ProductSales;
import com.gao.happying_shop_system.utils.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: GaoWenQiang
 * @Date: 2023/03/19 16:54
 * @Description:
 */
public interface IProductSalesService extends IService<ProductSales> {
    public R<List<ProductSales>> getSetmealSales(String beginTime, String endTime);
    public R<List<ProductSales>> getDishSales(String beginTime, String endTime);
    public R<List<Map<String, Object>>> getMoney(String beginTime, String endTime);
    public Integer getSalesNumber(String id);
    public R<ArrayList<Integer>> getUserNumbers(String beginTime, String endTime);
    public R<List<ProductSales>> getTop10Sales(String beginTime, String endTime);
    public R<List<Map<String, Object>>> getRegisterNumbers( String beginTime, String endTime);
}

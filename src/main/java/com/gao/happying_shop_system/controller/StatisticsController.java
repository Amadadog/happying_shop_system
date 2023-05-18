package com.gao.happying_shop_system.controller;

import com.gao.happying_shop_system.entity.ProductSales;
import com.gao.happying_shop_system.service.IAccessService;
import com.gao.happying_shop_system.service.IProductSalesService;
import com.gao.happying_shop_system.service.IUserService;
import com.gao.happying_shop_system.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: GaoWenQiang
 * @Date: 2023/03/21 21:11
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private IProductSalesService productSalesService;
    @Autowired
    private IAccessService accessService;
    @Autowired
    private IUserService userService;

    @GetMapping("/setmeal/sales")
    public R<List<ProductSales>> getSetmealSales(String beginTime, String endTime) throws MessagingException {
        return productSalesService.getSetmealSales(beginTime,endTime);
    }
    @GetMapping("/dish/sales")
    public R<List<ProductSales>> getDishSales(String beginTime, String endTime) throws MessagingException {
        return productSalesService.getDishSales(beginTime,endTime);
    }
    @GetMapping("/visits")
    public R<Integer> getVisits(String beginTime, String endTime) throws MessagingException {
        return accessService.getVisits(beginTime,endTime);
    }
    @GetMapping("/user/numbers")
    public R<ArrayList<Integer>> getUserNumbers(String beginTime, String endTime) {
        return  productSalesService.getUserNumbers(beginTime, endTime);
    }
    @GetMapping("/registerNumbers")
    public R<List<Map<String, Object>>> getRegisterNumbers( String beginTime, String endTime) {
        return  productSalesService.getRegisterNumbers(beginTime, endTime);
    }
    @GetMapping("/money")
    public R<List<Map<String, Object>>> getMoney(String beginTime, String endTime) {
        return  productSalesService.getMoney(beginTime, endTime);
    }
    @GetMapping("/top10Sales")
    public R<List<ProductSales>> getTop10Sales(String beginTime, String endTime) {
        return  productSalesService.getTop10Sales(beginTime, endTime);
    }
}

package com.gao.happying_shop_system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gao.happying_shop_system.utils.R;
import com.gao.happying_shop_system.entity.Orders;
import com.gao.happying_shop_system.service.IOrderDetailService;
import com.gao.happying_shop_system.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: GaoWenQiang
 * @Date: 2022/11/23 10:15
 * @Description:
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IOrderDetailService orderDetailService;
    /*@Autowired
    private IUserService userService;*/

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        log.info("订单:" + orders.toString());
        for (int i = 0; i < 100; i++) {
            orderService.submit(orders);
        }
        return R.success("444");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, Long number,String beginTime,String endTime) {
        return orderService.page(page,pageSize,number,beginTime,endTime);
    }
    @PutMapping
    public R<String> order(@RequestBody Orders orders) {
        log.info("====================");
        log.info("status:{},id:{}",orders.getStatus(),orders.getId());

        //订单状态 1待付款，2待派送，3已派送，4已完成，5已取消
        orderService.updateById(orders);
        if (orders.getStatus() == 3) {
            return R.success("订单已派送");
        } else if (orders.getStatus() == 4) {
            return R.success("已送达，订单已完成");
        } else if (orders.getStatus() == 5) {
            return R.success("订单已取消");
        }

        return R.success("订单状态已修改");
    }
}

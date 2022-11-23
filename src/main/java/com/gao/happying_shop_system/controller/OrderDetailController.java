package com.gao.happying_shop_system.controller;

import com.gao.happying_shop_system.service.IOrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: GaoWenQiang
 * @Date: 2022/11/23 10:17
 * @Description:
 */
@RestController
@RequestMapping("/orderDetail")
@Slf4j
public class OrderDetailController {

    @Autowired
    private IOrderDetailService orderDetailService;
}

package com.gao.happying_shop_system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gao.happying_shop_system.utils.R;
import com.gao.happying_shop_system.entity.ShoppingCart;
import com.gao.happying_shop_system.service.IShoppingCartService;
import com.gao.happying_shop_system.utils.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: GaoWenQiang
 * @Date: 2022/11/16 17:11
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/happying_shop_system/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private IShoppingCartService shoppingCartService;

    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        log.info("查看购物车...");
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        log.info(shoppingCart.toString());
        return shoppingCartService.add(shoppingCart);
    }
    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart) {
        log.info(shoppingCart.toString());
        return shoppingCartService.sub(shoppingCart);
    }

    @DeleteMapping("/clean")
    public R<String> clean() {
        return shoppingCartService.clean();
    }
}

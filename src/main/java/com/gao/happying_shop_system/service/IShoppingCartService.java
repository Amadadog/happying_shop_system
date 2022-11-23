package com.gao.happying_shop_system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gao.happying_shop_system.utils.R;
import com.gao.happying_shop_system.entity.ShoppingCart;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: GaoWenQiang
 * @Date: 2022/11/16 17:19
 * @Description:
 */
public interface IShoppingCartService extends IService<ShoppingCart> {
    public R<ShoppingCart> add(ShoppingCart shoppingCart);

    public R<ShoppingCart> sub(ShoppingCart shoppingCart);

    public R<String> clean();
}

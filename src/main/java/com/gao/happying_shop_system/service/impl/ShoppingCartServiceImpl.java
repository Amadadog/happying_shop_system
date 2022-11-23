package com.gao.happying_shop_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gao.happying_shop_system.utils.R;
import com.gao.happying_shop_system.entity.ShoppingCart;
import com.gao.happying_shop_system.mapper.ShoppingCartMapper;
import com.gao.happying_shop_system.service.IShoppingCartService;
import com.gao.happying_shop_system.utils.BaseContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: GaoWenQiang
 * @Date: 2022/11/16 17:21
 * @Description:
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements IShoppingCartService {
    //未实现：当同一商品传过来不同口味时，不应该只是数量加1，前端应该展示购物车的商品的口味
    @Override
    public R<ShoppingCart> add(ShoppingCart shoppingCart) {
        //设置当前购物车的userId
        shoppingCart.setUserId(BaseContext.getCurrentId());

        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        //如果添加的是套餐
        if (shoppingCart.getDishId()==null){
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        } else {
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }

        ShoppingCart shoppingCart1 = this.getOne(shoppingCartLambdaQueryWrapper);

        if (shoppingCart1 != null) {
            //查看此次请求添加的菜品是否已存在，如果有则数量+1
            shoppingCart1.setNumber(shoppingCart1.getNumber() + 1);
            this.updateById(shoppingCart1);
        } else {
            //没有则插入新数据，设置默认数量值为1
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            this.save(shoppingCart);
            shoppingCart1 = shoppingCart;
        }
        return R.success(shoppingCart1);
    }

    @Override
    public R<ShoppingCart> sub(ShoppingCart shoppingCart) {
        Long dishId = shoppingCart.getDishId();
        if(dishId != null) {
            //如果删除的是菜品
            LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId())
                    .eq(ShoppingCart::getDishId,dishId);
            ShoppingCart shoppingCart1 = this.getOne(shoppingCartLambdaQueryWrapper);
            Integer number = shoppingCart1.getNumber() - 1;
            shoppingCart1.setNumber(number);
            if (number > 0) {
                //如果数量大于0
                this.updateById(shoppingCart1);
            } else if(number == 0) {
                this.removeById(shoppingCart1.getId());
            } else if(number < 0) {
                return R.error("操作异常");
            }
            return R.success(shoppingCart1);
        }
        Long setmealId = shoppingCart.getSetmealId();
        if(setmealId != null) {
            //如果删除的是套餐  注：这里查询到可以用lambdaQuery实现，改为此方式
            ShoppingCart shoppingCart2 = this.lambdaQuery().eq(ShoppingCart::getUserId, BaseContext.getCurrentId())
                    .eq(ShoppingCart::getSetmealId, setmealId).one();
            Integer number = shoppingCart2.getNumber() - 1;
            shoppingCart2.setNumber(number);
            if (number > 0) {
                //如果数量大于0
                this.updateById(shoppingCart2);
            } else if(number == 0) {
                this.removeById(shoppingCart2.getId());
            } else if(number < 0) {
                return R.error("操作异常");
            }
            return R.success(shoppingCart2);
        }
        //如果传来的数据是空信息，或者不带有菜品和套餐的id
        return R.error("操作异常");
    }

    @Override
    public R<String> clean() {
        return this.lambdaUpdate()
                .eq(ShoppingCart::getUserId,BaseContext.getCurrentId())
                .remove()
                ? R.success("删除成功"): R.error("删除失败");
    }
}

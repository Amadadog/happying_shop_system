package com.gao.happying_shop_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gao.happying_shop_system.utils.R;
import com.gao.happying_shop_system.utils.ServiceException;
import com.gao.happying_shop_system.dto.OrdersDto;
import com.gao.happying_shop_system.entity.*;
import com.gao.happying_shop_system.mapper.OrderMapper;
import com.gao.happying_shop_system.service.*;
import com.gao.happying_shop_system.utils.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: GaoWenQiang
 * @Date: 2022/11/23 10:12
 * @Description:
 */
@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements IOrderService {

    @Autowired
    private IShoppingCartService shoppingCartService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IAddressService addressService;
    @Autowired
    private IOrderDetailService orderDetailService;
    @Autowired
    private IProductSalesService productSalesService;

    /**
     * @description: 下单
     * @param: [orders]
     * @return: com.gao.happying_shop_system.utils.R<java.lang.String>
     * @author: GaoWenQiang
     * @date: 2022/11/23 10:26
     */
    @Override
    @Transactional
    public R<String> submit(Orders orders) {
        //当前用户id
        Long userId = BaseContext.getCurrentId();

        //查询当前用户的购物车数据
        List<ShoppingCart> shoppingCarts = shoppingCartService.lambdaQuery().eq(ShoppingCart::getUserId, userId).list();
        if (shoppingCarts == null || shoppingCarts.size() == 0) {
            throw new ServiceException("购物车为空，不能下单");
        }
        //查询用户
        User user = userService.getById(userId);
        //查询地址
        AddressBook addressBook = addressService.getById(orders.getAddressBookId());
        if (addressBook == null) {
            throw new ServiceException("用户信息有误，不能下单");
        }
        //属性数据填充

        //订单号
        long orderId = IdWorker.getId();
        //保证多线程情况下，计算无误
        AtomicInteger amount = new AtomicInteger(0);
        List<OrderDetail> orderDetails = shoppingCarts.stream().map(shoppingCart -> {
            //插入到销量表
            ProductSales productSales = new ProductSales();
            // 当前遍历的购物车商品如果是dish
            if (shoppingCart.getDishId() != null) {
                productSales.setDishId(shoppingCart.getDishId());
                productSales.setDishNumber(shoppingCart.getNumber());
//                productSales.setCreateTime(LocalDateTime.of(2023, 4, 3, 13, 29, 10));
                productSales.setCreateTime(LocalDateTime.now());
                productSales.setAllSalesAmount(shoppingCart.getAmount().multiply(new BigDecimal(shoppingCart.getNumber())));
            }
            // 当前遍历的购物车商品如果是setmeal
            if (shoppingCart.getSetmealId() != null) {
                productSales.setSetmealId(shoppingCart.getSetmealId());
                productSales.setSetmealNumber(shoppingCart.getNumber());
//                productSales.setCreateTime(LocalDateTime.of(2023, 4, 3, 13, 29, 10));
                productSales.setCreateTime(LocalDateTime.now());
                productSales.setAllSalesAmount(shoppingCart.getAmount().multiply(new BigDecimal(shoppingCart.getNumber())));
            }
            productSalesService.save(productSales);

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(shoppingCart.getNumber());
            orderDetail.setDishFlavor(shoppingCart.getDishFlavor());
            orderDetail.setDishId(shoppingCart.getDishId());
            orderDetail.setSetmealId(shoppingCart.getSetmealId());
            orderDetail.setName(shoppingCart.getName());
            orderDetail.setImage(shoppingCart.getImage());
            orderDetail.setAmount(shoppingCart.getAmount());
            amount.addAndGet(shoppingCart.getAmount().multiply(new BigDecimal(shoppingCart.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());

        orders.setNumber(String.valueOf(orderId));
        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));
        orders.setUserId(userId);
        orders.setNumber(String.valueOf(orderId));
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));

        //订单表插入数据
        this.save(orders);
        //明细表插入数据
        orderDetailService.saveBatch(orderDetails);
        //清空购物车
//        shoppingCartService.clean();
        return R.success("下单成功");
    }

    @Override
    public R<Page> page(int page, int pageSize, Long number, String beginTime, String endTime) {
        Page<Orders> ordersPage = new Page<>(page, pageSize);
        Page<OrdersDto> ordersDtoPage = new Page<>();
        try {
            LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<>();
            ordersLambdaQueryWrapper.like(number != null, Orders::getNumber, number)
                    .ge(beginTime != null, Orders::getOrderTime, beginTime)
                    .le(endTime != null, Orders::getOrderTime, endTime);
            ordersLambdaQueryWrapper.orderByDesc(Orders::getOrderTime);
            this.page(ordersPage, ordersLambdaQueryWrapper);
            //对象拷贝
            BeanUtils.copyProperties(ordersPage, ordersDtoPage, "records");
            List<Orders> records = ordersPage.getRecords();
            List<OrdersDto> ordersDtoList = records.stream().map(record -> {
                OrdersDto ordersDto = new OrdersDto();
                BeanUtils.copyProperties(record, ordersDto);
            /*//查询用户名称
            User user = userService.lambdaQuery().eq(record.getUserId() != null, User::getId, record.getUserId()).one();
            ordersDto.setUserName(user.getName());*/
                List<OrderDetail> orderDetails = orderDetailService.lambdaQuery()
                        .eq(OrderDetail::getOrderId, record.getId())
                        .list();
                ordersDto.setOrderDetails(orderDetails);
                return ordersDto;
            }).collect(Collectors.toList());
            ordersDtoPage.setRecords(ordersDtoList);
        } catch (Exception e) {
            log.info(e.getMessage());
            return R.error("查询失败");
        }
        return R.success(ordersDtoPage);
    }
}

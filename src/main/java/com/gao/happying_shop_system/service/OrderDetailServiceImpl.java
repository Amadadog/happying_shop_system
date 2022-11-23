package com.gao.happying_shop_system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gao.happying_shop_system.entity.OrderDetail;
import com.gao.happying_shop_system.mapper.OrderDetailMapper;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: GaoWenQiang
 * @Date: 2022/11/23 10:14
 * @Description:
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements IOrderDetailService {
}

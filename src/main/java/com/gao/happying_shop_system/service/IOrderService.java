package com.gao.happying_shop_system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gao.happying_shop_system.utils.R;
import com.gao.happying_shop_system.entity.Orders;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: GaoWenQiang
 * @Date: 2022/11/23 10:11
 * @Description:
 */
public interface IOrderService extends IService<Orders> {
    /**
    * @description: 用户下单
    * @param: [orders]
    * @return: com.gao.happying_shop_system.utils.R<java.lang.String>
    * @author: GaoWenQiang
    * @date: 2022/11/23 15:56
    */
    public R<String> submit(Orders orders);
    /**
    * @description: 前后台通用明细
    * @param: [page, pageSize, number, beginTime, endTime]
    * @return: com.gao.happying_shop_system.utils.R<com.baomidou.mybatisplus.extension.plugins.pagination.Page>
    * @author: GaoWenQiang
    * @date: 2022/11/23 15:59
    */
    public R<Page> page(int page, int pageSize, Long number,String beginTime,String endTime);
}

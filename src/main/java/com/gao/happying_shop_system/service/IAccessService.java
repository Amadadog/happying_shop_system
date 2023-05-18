package com.gao.happying_shop_system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gao.happying_shop_system.entity.AccessLog;
import com.gao.happying_shop_system.entity.ProductSales;
import com.gao.happying_shop_system.utils.R;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: GaoWenQiang
 * @Date: 2023/03/18 17:17
 * @Description:
 */
public interface IAccessService extends IService<AccessLog> {

    public R<Integer> getVisits(String beginTime, String endTime);
}

package com.gao.happying_shop_system.dto;


import com.gao.happying_shop_system.entity.OrderDetail;
import com.gao.happying_shop_system.entity.Orders;
import lombok.Data;
import java.util.List;
/**
* @Description:
* @Param:
* @return:
* @Author: GaoWenQiang
* @Date: 2022/11/3 10:11
*/
@Data
public class OrdersDto extends Orders {

    private List<OrderDetail> orderDetails;
	
}

package com.gao.happying_shop_system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gao.happying_shop_system.entity.AddressBook;
import com.gao.happying_shop_system.mapper.AddressMapper;
import com.gao.happying_shop_system.service.IAddressService;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: GaoWenQiang
 * @Date: 2022/11/15 16:45
 * @Description:
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, AddressBook> implements IAddressService {
}

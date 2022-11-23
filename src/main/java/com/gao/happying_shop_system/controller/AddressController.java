package com.gao.happying_shop_system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.gao.happying_shop_system.utils.R;
import com.gao.happying_shop_system.entity.AddressBook;
import com.gao.happying_shop_system.service.IAddressService;
import com.gao.happying_shop_system.utils.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: GaoWenQiang
 * @Date: 2022/11/15 16:52
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressController {
    @Autowired
    private IAddressService addressService;

    /**
     * @description: 新增地址
     * @param: [addressBook]
     * @return: com.gao.happying_shop_system.utils.R<java.lang.String>
     * @author: GaoWenQiang
     * @date: 2022/11/15 17:06
     */
    @PostMapping
    public R<String> save(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info(addressBook.toString());
        addressService.save(addressBook);
        return R.success("");
    }

    /**
     * @description: <批量>删除地址
     * @param: [ids]
     * @return: com.gao.happying_shop_system.utils.R<java.lang.String>
     * @author: GaoWenQiang
     * @date: 2022/11/15 17:19
     */
    @DeleteMapping
    public R<String> delete(@RequestParam ArrayList<Long> ids) {
        addressService.removeByIds(ids);
        return R.success("删除成功");
    }
    /**
    * @description: 设置默认地址
    * @param: [addressBook]
    * @return: com.gao.happying_shop_system.utils.R<java.lang.String>
    * @author: GaoWenQiang
    * @date: 2022/11/16 10:17
    */
    @PutMapping("/default")
    public R<String> setDefault(@RequestBody AddressBook addressBook) {
        LambdaUpdateWrapper<AddressBook> addressUpdateWrapper = new LambdaUpdateWrapper<>();
        addressUpdateWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        addressUpdateWrapper.set(AddressBook::getIsDefault,0);
        addressService.update(addressUpdateWrapper);

        addressBook.setIsDefault(1);
        addressService.updateById(addressBook);
        return R.success("设置默认成功");
    }
    /**
    * @description: 根据id查询地址
    * @param: [id]
    * @return: com.gao.happying_shop_system.utils.R<java.lang.Object>
    * @author: GaoWenQiang
    * @date: 2022/11/16 10:19
    */
    @GetMapping("/{id}")
    public R<Object> getAddress(@PathVariable Long id) {
        AddressBook addressBook = addressService.getById(id);
        return addressBook != null?R.success(addressBook):R.error("没有相关信息");

    }
    /**
    * @description: 查找默认地址
    * @param: []
    * @return: com.gao.happying_shop_system.utils.R<com.gao.happying_shop_system.entity.AddressBook>
    * @author: GaoWenQiang
    * @date: 2022/11/16 10:24
    */
    @GetMapping("/default")
    public R<AddressBook> getDefault(){
        LambdaQueryWrapper<AddressBook> addressBookLambdaQueryWrapper = new LambdaQueryWrapper<>();
        addressBookLambdaQueryWrapper.eq(AddressBook::getIsDefault,1);
        addressBookLambdaQueryWrapper.eq(AddressBook::getUserId,BaseContext.getCurrentId());
        AddressBook addressBook = addressService.getOne(addressBookLambdaQueryWrapper);
        return addressBook != null? R.success(addressBook): R.error("没有相关信息");

    }
    /**
    * @description: 查询该用户下的所有地址
    * @param: []
    * @return: com.gao.happying_shop_system.utils.R<java.util.List<com.gao.happying_shop_system.entity.AddressBook>>
    * @author: GaoWenQiang
    * @date: 2022/11/16 10:50
    */
    @GetMapping("/list")
    public R<List<AddressBook>> listUserAllAddress(){
        LambdaQueryWrapper<AddressBook> addressBookLambdaQueryWrapper = new LambdaQueryWrapper<>();
        addressBookLambdaQueryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        addressBookLambdaQueryWrapper.orderByDesc(AddressBook::getIsDefault).orderByDesc(AddressBook::getUpdateTime);
        List<AddressBook> addressBooks = addressService.list(addressBookLambdaQueryWrapper);
        return R.success(addressBooks);
    }

    @PutMapping
    public R<String> update(@RequestBody AddressBook addressBook) {
        return addressService.updateById(addressBook)?R.success("修改成功"):R.error("修改失败");
    }
}

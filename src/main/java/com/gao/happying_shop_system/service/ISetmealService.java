package com.gao.happying_shop_system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gao.happying_shop_system.utils.R;
import com.gao.happying_shop_system.dto.SetmealDto;
import com.gao.happying_shop_system.entity.Setmeal;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: GaoWenQiang
 * @Date: 2022/09/22 15:21
 * @Description:
 */
public interface ISetmealService extends IService<Setmeal> {
    /**
    * @description: 新增套餐，同时需要保存套餐和商品之间的关联关系
    * @param: []
    * @return: void
    * @author: GaoWenQiang
    * @date: 2022/11/8 16:28
    */
    public void saveWithDish(SetmealDto setmealDto);

    public R<Page> Page(int page, int pageSize, String name);

    public R<String> updateWithDish(SetmealDto setmealDto);

    public SetmealDto getByIdWithSetmealAndDish(Long id);

    public void delete(@RequestParam List<Long> ids);

    public R<List<Setmeal>> list(Setmeal setmeal);
}

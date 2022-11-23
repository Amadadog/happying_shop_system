package com.gao.happying_shop_system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gao.happying_shop_system.utils.R;
import com.gao.happying_shop_system.entity.Category;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: GaoWenQiang
 * @Date: 2022/09/14 21:02
 * @Description:
 */
public interface ICategoryService extends IService<Category> {
    public R<String> saveCategory(Category category);
    public R<Page> page(int page, int pageSize);
    public void delete(Long ids);
}

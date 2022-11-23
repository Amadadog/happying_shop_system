package com.gao.happying_shop_system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gao.happying_shop_system.utils.R;
import com.gao.happying_shop_system.entity.User;

import javax.servlet.http.HttpSession;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: GaoWenQiang
 * @Date: 2022/11/11 16:37
 * @Description:
 */
public interface IUserService extends IService<User> {
    public R<String> loginByUser(User user, HttpSession httpSession);
}
